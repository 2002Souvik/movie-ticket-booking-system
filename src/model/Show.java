package model;

import java.time.LocalDateTime;

public class Show {
    private String id;
    private Movie movie;
    private Theater theater;
    private Theater.Screen screen;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double price;
    
    public Show(String id, Movie movie, Theater theater, Theater.Screen screen, 
                LocalDateTime startTime, double price) {
        this.id = id;
        this.movie = movie;
        this.theater = theater;
        this.screen = screen;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(movie.getDuration());
        this.price = price;
    }
    
    // Getters
    public String getId() { return id; }
    public Movie getMovie() { return movie; }
    public Theater getTheater() { return theater; }
    public Theater.Screen getScreen() { return screen; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public double getPrice() { return price; }
}
