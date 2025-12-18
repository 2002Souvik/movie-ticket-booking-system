package service;

import model.*;
import repository.BookingRepository;
import repository.ShowRepository;
import util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BookingService {
    private BookingRepository bookingRepository;
    private ShowRepository showRepository;
    private SeatLockService seatLockService;
    private PaymentService paymentService;
    
    public BookingService(BookingRepository bookingRepository, ShowRepository showRepository,
                         SeatLockService seatLockService, PaymentService paymentService) {
        this.bookingRepository = bookingRepository;
        this.showRepository = showRepository;
        this.seatLockService = seatLockService;
        this.paymentService = paymentService;
    }
    
    public Booking createBooking(User user, String showId, List<String> seatNumbers) {
        Show show = showRepository.getShow(showId);
        if (show == null) {
            throw new IllegalArgumentException("Show not found");
        }
        
        // Get seat objects
        List<Seat> selectedSeats = show.getScreen().getSeats().stream()
                .filter(seat -> seatNumbers.contains(seat.getSeatNumber()))
                .collect(Collectors.toList());
        
        if (selectedSeats.size() != seatNumbers.size()) {
            throw new IllegalArgumentException("Some seats not found");
        }
        
        // Check if any seats are already booked
        if (selectedSeats.stream().anyMatch(seat -> seat.getStatus() == SeatStatus.BOOKED)) {
            throw new IllegalStateException("Some seats are already booked");
        }
        
        // Check seat availability and lock seats
        if (seatLockService.areSeatsLocked(show, selectedSeats, user.getId())) {
            throw new IllegalStateException("Some seats are already locked by another user");
        }
        
        if (!seatLockService.lockSeats(show, selectedSeats, user.getId())) {
            throw new IllegalStateException("Failed to lock seats");
        }
        
        try {
            double totalAmount = selectedSeats.size() * show.getPrice();
            String bookingId = IdGenerator.generateId("BKG");
            Booking booking = new Booking(bookingId, user, show, selectedSeats, totalAmount);
            
            bookingRepository.addBooking(booking);
            user.addBooking(booking);
            
            return booking;
        } catch (Exception e) {
            // Unlock seats if booking creation fails
            seatLockService.unlockSeats(show, selectedSeats, user.getId());
            throw e;
        }
    }
    
    public boolean confirmBooking(String bookingId, PaymentMethod paymentMethod) {
        Booking booking = bookingRepository.getBooking(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not in pending state");
        }
        
        if (LocalDateTime.now().isAfter(booking.getExpiryTime())) {
            booking.setStatus(BookingStatus.EXPIRED);
            seatLockService.unlockSeats(booking.getShow(), booking.getSeats(), booking.getUser().getId());
            bookingRepository.updateBooking(booking);
            throw new IllegalStateException("Booking has expired");
        }
        
        // Process payment
        boolean paymentSuccess = paymentService.processPayment(booking, paymentMethod);
        
        if (paymentSuccess) {
            booking.setStatus(BookingStatus.CONFIRMED);
            // Mark seats as booked
            for (Seat seat : booking.getSeats()) {
                seat.setStatus(SeatStatus.BOOKED);
            }
            bookingRepository.updateBooking(booking);
            return true;
        } else {
            booking.setStatus(BookingStatus.CANCELLED);
            seatLockService.unlockSeats(booking.getShow(), booking.getSeats(), booking.getUser().getId());
            bookingRepository.updateBooking(booking);
            return false;
        }
    }
    
    public void cancelBooking(String bookingId) {
        Booking booking = bookingRepository.getBooking(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            // Process refund
            paymentService.processRefund(booking);
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        seatLockService.unlockSeats(booking.getShow(), booking.getSeats(), booking.getUser().getId());
        
        // If seats were booked, mark them as available again
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            for (Seat seat : booking.getSeats()) {
                seat.setStatus(SeatStatus.AVAILABLE);
            }
        }
        
        bookingRepository.updateBooking(booking);
    }
    
    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.getUserBookings(userId);
    }
}