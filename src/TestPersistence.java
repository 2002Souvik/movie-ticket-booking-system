import model.*;
import repository.*;
import util.IdGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TestPersistence {
    public static void main(String[] args) {
        // setup repositories
        MovieRepository movieRepo = new MovieRepository();
        ShowRepository showRepo = new ShowRepository();
        UserRepository userRepo = new UserRepository();
        BookingRepository bookingRepo = new BookingRepository(showRepo, userRepo);

        // create movie, theater, screen, show
        Movie movie = new Movie(IdGenerator.generateId("MOV"), "Test Movie", "Desc", 100, "Drama", "English",
                LocalDate.now(), 7.5, "Actor");
        movieRepo.addMovie(movie);

        Theater theater = new Theater("T_TEST", "Test Theater", "Nowhere");
        Theater.Screen screen = new Theater.Screen("S_TEST", "Screen Test", 20);
        theater.addScreen(screen);

        Show show = new Show(IdGenerator.generateId("SHW"), movie, theater, screen, LocalDateTime.now().plusHours(1), 100.0);
        showRepo.addShow(show);

        // create user
        User user = new User(IdGenerator.generateId("USR"), "Alice", "alice@test.com", "9999999999");
        userRepo.addUser(user);

        // create booking for first two seats
        List<Seat> seats = show.getScreen().getSeats().subList(0, 2);
        Booking booking = new Booking(IdGenerator.generateId("BKG"), user, show, seats, seats.size() * show.getPrice());
        bookingRepo.addBooking(booking);

        System.out.println("Wrote booking and user. CSV contents:");
        try {
            System.out.println("--- users.csv ---");
            java.nio.file.Files.lines(java.nio.file.Paths.get("data/users.csv")).forEach(System.out::println);
            System.out.println("--- logins.csv ---");
            java.nio.file.Files.lines(java.nio.file.Paths.get("data/logins.csv")).forEach(System.out::println);
            System.out.println("--- bookings.csv ---");
            java.nio.file.Files.lines(java.nio.file.Paths.get("data/bookings.csv")).forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error reading CSVs: " + e.getMessage());
        }
    }
}
