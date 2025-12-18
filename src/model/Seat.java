package model;

public class Seat {
    private String seatNumber;
    private SeatType type;
    private SeatStatus status;
    
    public Seat(String seatNumber, SeatType type) {
        this.seatNumber = seatNumber;
        this.type = type;
        this.status = SeatStatus.AVAILABLE;
    }
    
    // Getters and Setters
    public String getSeatNumber() { return seatNumber; }
    public SeatType getType() { return type; }
    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }
}
