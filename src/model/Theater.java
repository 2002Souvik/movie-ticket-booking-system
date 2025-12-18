package model;

import java.util.ArrayList;
import java.util.List;

public class Theater {
    private String id;
    private String name;
    private String location;
    private List<Screen> screens;
    
    public Theater(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.screens = new ArrayList<>();
    }
    
    public void addScreen(Screen screen) {
        screens.add(screen);
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public List<Screen> getScreens() { return screens; }

    public static class Screen {
        private String id;
        private String name;
        private int totalSeats;
        private List<Seat> seats;
        
        public Screen(String id, String name, int totalSeats) {
            this.id = id;
            this.name = name;
            this.totalSeats = totalSeats;
            this.seats = new ArrayList<>();
            initializeSeats();
        }
        
        private void initializeSeats() {
            int rows = totalSeats / 10;
            for (int i = 0; i < rows; i++) {
                for (int j = 1; j <= 10; j++) {
                    String seatNumber = (char) ('A' + i) + String.valueOf(j);
                    seats.add(new Seat(seatNumber, SeatType.REGULAR));
                }
            }
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public int getTotalSeats() { return totalSeats; }
        public List<Seat> getSeats() { return seats; }
    }
}
