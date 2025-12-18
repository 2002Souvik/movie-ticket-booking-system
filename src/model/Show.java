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

    // Return a friendly time type (Morning/Afternoon/Evening/Night)
    public String getTimeType() {
        int hour = startTime.getHour();
        if (hour >= 5 && hour < 12) return "Morning";
        if (hour >= 12 && hour < 17) return "Afternoon";
        if (hour >= 17 && hour < 21) return "Evening";
        return "Night";
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
