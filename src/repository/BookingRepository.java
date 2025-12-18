package repository;

import model.Booking;
import java.util.*;

public class BookingRepository {
    private Map<String, Booking> bookings;
    private Map<String, List<Booking>> userBookings; // userId -> list of bookings
    
    public BookingRepository() {
        this.bookings = new HashMap<>();
        this.userBookings = new HashMap<>();
    }
    
    public void addBooking(Booking booking) {
        String bookingId = booking.getId();
        String userId = booking.getUser().getId();
        
        bookings.put(bookingId, booking);
        userBookings.computeIfAbsent(userId, k -> new ArrayList<>()).add(booking);
    }
    
    public Booking getBooking(String id) {
        return bookings.get(id);
    }
    
    public List<Booking> getUserBookings(String userId) {
        return userBookings.getOrDefault(userId, new ArrayList<>());
    }
    
    public void updateBooking(Booking booking) {
        bookings.put(booking.getId(), booking);
    }
}