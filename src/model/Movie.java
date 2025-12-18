package model;

import java.time.LocalDate;

public class Movie {
    private String id;
    private String title;
    private String description;
    private int duration; // in minutes
    private String genre;
    private String language;
    private LocalDate releaseDate;
    private double rating;
    private String cast;
    
    public Movie(String id, String title, String description, int duration, 
                 String genre, String language, LocalDate releaseDate, 
                 double rating, String cast) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.genre = genre;
        this.language = language;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.cast = cast;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public String getGenre() { return genre; }
    public String getLanguage() { return language; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public double getRating() { return rating; }
    public String getCast() { return cast; }
}