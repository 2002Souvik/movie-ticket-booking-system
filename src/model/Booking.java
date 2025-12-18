package model;

import java.time.LocalDateTime;
import java.util.List;

public class Booking {
    private String id;
    private User user;
    private Show show;
    private List<Seat> seats;
    private double totalAmount;
    private BookingStatus status;
    private LocalDateTime bookingTime;
    private LocalDateTime expiryTime;
    
    public Booking(String id, User user, Show show, List<Seat> seats, 
                   double totalAmount) {
        this.id = id;
        this.user = user;
        this.show = show;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.status = BookingStatus.PENDING;
        this.bookingTime = LocalDateTime.now();
        this.expiryTime = bookingTime.plusMinutes(5); // 5 minutes to complete payment
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public User getUser() { return user; }
    public Show getShow() { return show; }
    public List<Seat> getSeats() { return seats; }
    public double getTotalAmount() { return totalAmount; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public LocalDateTime getExpiryTime() { return expiryTime; }
}
