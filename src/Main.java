import model.*;
import repository.*;
import service.*;
import util.IdGenerator;
import util.CsvUtil;
import util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static MovieService movieService;
    private static ShowService showService;
    private static BookingService bookingService;
    private static UserRepository userRepository;
    private static SeatLockService seatLockService;
    private static PaymentService paymentService;
    
    private static User currentUser;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        initializeServices();
        showMainMenu();
    }
        
    
    private static void initializeServices() {
        // Initialize repositories
        MovieRepository movieRepository = new MovieRepository();
        ShowRepository showRepository = new ShowRepository();
        userRepository = new UserRepository();
        BookingRepository bookingRepository = new BookingRepository(showRepository, userRepository);
        
        // Initialize services
        movieService = new MovieService(movieRepository);
        showService = new ShowService(showRepository);
        seatLockService = new SeatLockService();
        paymentService = new PaymentService();
        bookingService = new BookingService(bookingRepository, showRepository, 
                                          seatLockService, paymentService);
        
        // Add sample data
        initializeSampleData();
    }
    
    private static void initializeSampleData() {
        // Add sample movies (Bollywood, Tollywood, Hollywood mix)
        Movie movie1 = new Movie(IdGenerator.generateId("MOV"), "3 Idiots", 
            "Two friends search for their lost companion and recall their college days", 170, "Comedy/Drama", 
            "Hindi", LocalDate.of(2009, 12, 25), 8.4, "Aamir Khan, R. Madhavan");

        Movie movie2 = new Movie(IdGenerator.generateId("MOV"), "Dangal", 
            "A former wrestler trains his daughters to become world-class wrestlers", 161, "Drama/Sports", "Hindi", 
            LocalDate.of(2016, 12, 23), 8.6, "Aamir Khan, Sakshi Tanwar");

        Movie movie3 = new Movie(IdGenerator.generateId("MOV"), "RRR", 
            "A fictional tale about two Indian revolutionaries", 182, "Action/Drama", "Telugu", 
            LocalDate.of(2022, 3, 25), 8.0, "N. T. Rama Rao Jr., Ram Charan");

        Movie movie4 = new Movie(IdGenerator.generateId("MOV"), "Baahubali: The Beginning", 
            "An epic tale of a lost prince and a kingdom in turmoil", 159, "Action/Fantasy", "Telugu", 
            LocalDate.of(2015, 7, 10), 8.0, "Prabhas, Rana Daggubati");

        Movie movie5 = new Movie(IdGenerator.generateId("MOV"), "Inception", 
            "A thief who steals corporate secrets through dream-sharing", 148, "Sci-Fi", 
            "English", LocalDate.of(2010, 7, 16), 8.8, "Leonardo DiCaprio, Joseph Gordon-Levitt");

        Movie movie6 = new Movie(IdGenerator.generateId("MOV"), "Interstellar", 
            "Explorers travel through a wormhole in space to ensure humanity's survival", 169, "Sci-Fi", "English", 
            LocalDate.of(2014, 11, 7), 8.6, "Matthew McConaughey, Anne Hathaway");

        movieService.addMovie(movie1);
        movieService.addMovie(movie2);
        movieService.addMovie(movie3);
        movieService.addMovie(movie4);
        movieService.addMovie(movie5);
        movieService.addMovie(movie6);
        
        // Add sample theaters
        Theater theater1 = new Theater("T1", "PVR Cinemas", "Downtown Mall");
        Theater theater2 = new Theater("T2", "INOX", "City Center");
        
        // Add screens to theaters
        Theater.Screen screen1 = new Theater.Screen("S1", "Screen 1", 50);
        Theater.Screen screen2 = new Theater.Screen("S2", "Screen 2", 40);
        Theater.Screen screen3 = new Theater.Screen("S3", "IMAX", 100);
        
        theater1.addScreen(screen1);
        theater1.addScreen(screen2);
        theater2.addScreen(screen3);
        
        // Add sample shows
        showService.createShow(IdGenerator.generateId("SHW"), movie1, theater1, screen1, 
                             LocalDateTime.now().plusHours(2), 12.50);
        showService.createShow(IdGenerator.generateId("SHW"), movie2, theater1, screen2, 
                             LocalDateTime.now().plusHours(4), 10.00);
        showService.createShow(IdGenerator.generateId("SHW"), movie3, theater2, screen3, 
                             LocalDateTime.now().plusHours(6), 15.00);
        showService.createShow(IdGenerator.generateId("SHW"), movie1, theater2, screen3, 
                             LocalDateTime.now().plusHours(8), 12.50);

        // Add sample user only if not already registered
        String sampleEmail = "john@email.com";
        if (!userRepository.userExists(sampleEmail)) {
            User sampleUser = new User("USR_001", "John Doe", sampleEmail, "1234567890");
            userRepository.addUser(sampleUser);
        }
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("Sample user: john@email.com");
    }
    
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== Movie Ticket Booking System ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Browse Movies");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    browseMovies();
                    break;
                case 4:
                    System.out.println("Thank you for using our service!");
                    seatLockService.shutdown();
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
    
    private static void registerUser() {
        System.out.println("\n=== User Registration ===");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        
        if (userRepository.userExists(email)) {
            System.out.println("User already exists with this email!");
            return;
        }
        
        String userId = IdGenerator.generateId("USR");
        User user = new User(userId, name, email, phone);
        userRepository.addUser(user);
        System.out.println("Registration successful! User ID: " + userId);
    }
    
    private static void loginUser() {
        System.out.println("\n=== User Login ===");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        currentUser = userRepository.getUserByEmail(email);
        if (currentUser != null) {
            System.out.println("Login successful! Welcome " + currentUser.getName());
            // log login
            CsvUtil.appendLine("data/logins.csv", DateTimeUtil.format(LocalDateTime.now()) + "," + currentUser.getId() + "," + currentUser.getEmail() + ",LOGIN");
            showUserMenu();
        } else {
            System.out.println("User not found! Please register first.");
        }
    }
    
    private static void browseMovies() {
        List<Movie> movies = movieService.getAllMovies();
        System.out.println("\n=== Available Movies ===");
        
        if (movies.isEmpty()) {
            System.out.println("No movies available at the moment.");
            return;
        }
        
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.println((i + 1) + ". " + movie.getTitle() + 
                             " | " + movie.getGenre() + 
                             " | Rating: " + movie.getRating() +
                             " | Duration: " + movie.getDuration() + "min");
        }
        
        if (currentUser != null) {
            System.out.print("Enter movie number to view shows (0 to go back): ");
            int choice = getIntInput();
            
            if (choice > 0 && choice <= movies.size()) {
                viewShowsForMovie(movies.get(choice - 1));
            }
        } else {
            System.out.println("Please login to book tickets!");
        }
    }
    
    
    private static void viewShowsForMovie(Movie movie) {
        List<Show> shows = showService.getShowsByMovie(movie.getId());
        System.out.println("\n=== Shows for " + movie.getTitle() + " ===");
        
        if (shows.isEmpty()) {
            System.out.println("No shows available for this movie.");
            return;
        }
        
        for (int i = 0; i < shows.size(); i++) {
            Show show = shows.get(i);
            String date = show.getStartTime().toLocalDate().toString();
            String time = DateTimeUtil.formatTime(show.getStartTime());
            String type = show.getTimeType();
            System.out.println((i + 1) + ". " + show.getTheater().getName() + 
                             " | " + show.getScreen().getName() +
                             " | " + date + " " + time + " (" + type + ")"
                             + " | Rs: " + show.getPrice());
        }
        
        System.out.print("Enter show number to book (0 to go back): ");
        int choice = getIntInput();
        
        if (choice > 0 && choice <= shows.size()) {
            bookTickets(shows.get(choice - 1));
        }
    }
    
    private static void bookTickets(Show show) {
        System.out.println("\n=== Booking for " + show.getMovie().getTitle() + " ===");
        System.out.println("Theater: " + show.getTheater().getName() + " - " + show.getTheater().getLocation());
        System.out.println("Screen: " + show.getScreen().getName());
        System.out.println("Time: " + show.getStartTime());
        System.out.println("Price per ticket: Rs: " + show.getPrice());
        
        // Display seat layout
        displaySeatLayout(show);
        
        System.out.print("Enter seat numbers (comma separated, e.g., A1,A2): ");
        String seatInput = scanner.nextLine().trim().toUpperCase();
        List<String> seatNumbers = Arrays.asList(seatInput.split("\\s*,\\s*"));
        
        try {
            Booking booking = bookingService.createBooking(currentUser, show.getId(), seatNumbers);
            System.out.println("\nSeats locked successfully!");
            System.out.println("Booking ID: " + booking.getId());
            System.out.println("Selected Seats: " + String.join(", ", seatNumbers));
            System.out.println("Total amount: Rs: " + booking.getTotalAmount());
            System.out.println("You have 5 minutes to complete payment.");
            
            System.out.print("Proceed to payment? (yes/no): ");
            String paymentChoice = scanner.nextLine();
            
            if ("yes".equalsIgnoreCase(paymentChoice)) {
                System.out.println("\nSelect payment method:");
                System.out.println("1. Credit Card");
                System.out.println("2. Debit Card"); 
                System.out.println("3. UPI");
                System.out.println("4. Wallet");
                System.out.print("Choose option (1-4): ");
                
                int pmtChoice = getIntInput();
                if (pmtChoice >= 1 && pmtChoice <= 4) {
                    PaymentMethod paymentMethod = PaymentMethod.values()[pmtChoice - 1];
                    System.out.println("Processing payment...");
                    
                    boolean success = bookingService.confirmBooking(booking.getId(), paymentMethod);
                    
                    if (success) {
                        System.out.println(" Payment successful! Booking confirmed!");
                        System.out.println(" Tickets booked successfully! Enjoy your movie!");
                    } else {
                        System.out.println(" Payment failed! Booking cancelled.");
                    }
                } else {
                    System.out.println("Invalid payment method! Booking cancelled.");
                    bookingService.cancelBooking(booking.getId());
                }
            } else {
                bookingService.cancelBooking(booking.getId());
                System.out.println("Booking cancelled.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displaySeatLayout(Show show) {
        System.out.println("\n=== Seat Layout ===");
        System.out.println(" Available | Booked | Locked");
        System.out.println("   SCREEN ");
        System.out.println("====================");
        
        List<Seat> seats = show.getScreen().getSeats();
        int seatsPerRow = 10;
        int rowCount = show.getScreen().getTotalSeats() / seatsPerRow;
        
        for (int i = 0; i < rowCount; i++) {
            System.out.print((char)('A' + i) + "  ");
            for (int j = 0; j < seatsPerRow; j++) {
                int index = i * seatsPerRow + j;
                if (index < seats.size()) {
                    Seat seat = seats.get(index);
                    String statusSymbol = switch (seat.getStatus()) {
                        case AVAILABLE -> "🟢";
                        case BOOKED -> "🔴";
                        case LOCKED -> "🟡";
                    };
                    System.out.print(statusSymbol + " ");
                }
            }
            System.out.println();
        }
        
        // Print seat numbers
        System.out.print("   ");
        for (int j = 1; j <= seatsPerRow; j++) {
            System.out.print(j + "  ");
        }
        System.out.println();
    }
    
    private static void viewBookingHistory() {
        List<Booking> bookings = bookingService.getUserBookings(currentUser.getId());
        System.out.println("\n=== Your Booking History ===");
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found!");
        } else {
            for (Booking booking : bookings) {
                System.out.println("┌─────────────────────────────────────");
                System.out.println("│ Booking ID: " + booking.getId());
                System.out.println("│ Movie: " + booking.getShow().getMovie().getTitle());
                System.out.println("│ Theater: " + booking.getShow().getTheater().getName());
                System.out.println("│ Seats: " + booking.getSeats().stream()
                        .map(Seat::getSeatNumber)
                        .reduce((a, b) -> a + ", " + b).orElse(""));
                System.out.println("│ Amount: Rs: " + booking.getTotalAmount());
                System.out.println("│ Status: " + booking.getStatus());
                System.out.println("│ Time: " + DateTimeUtil.format(booking.getBookingTime()));
                System.out.println("└─────────────────────────────────────");
            }
        }
    }

    private static void cancelBookingMenu() {
        List<Booking> bookings = bookingService.getUserBookings(currentUser.getId());
        if (bookings.isEmpty()) {
            System.out.println("No bookings to cancel.");
            return;
        }

        System.out.println("\nYour bookings:");
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            System.out.printf("%d. %s | %s | Seats: %s | Status: %s\n", i+1, b.getId(), b.getShow().getMovie().getTitle(),
                    b.getSeats().stream().map(Seat::getSeatNumber).reduce((a,b2)->a+", "+b2).orElse(""), b.getStatus());
        }

        System.out.print("Enter booking number to cancel (0 to go back): ");
        int choice = getIntInput();
        if (choice <= 0 || choice > bookings.size()) {
            System.out.println("Cancelled.");
            return;
        }

        Booking toCancel = bookings.get(choice - 1);
        try {
            bookingService.cancelBooking(toCancel.getId());
            System.out.println("Booking " + toCancel.getId() + " cancelled successfully.");
        } catch (Exception e) {
            System.out.println("Failed to cancel booking: " + e.getMessage());
        }
    }
    
    private static void showUserMenu() {
        while (currentUser != null) {
            System.out.println("\n=== Welcome, " + currentUser.getName() + " ===");
            System.out.println("1. Browse Movies & Book Tickets");
            System.out.println("2. View Booking History");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Logout");
            System.out.print("Choose option: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    browseMovies();
                    break;
                case 2:
                    viewBookingHistory();
                    break;
                case 3:
                    cancelBookingMenu();
                    break;
                case 4:
                    // log logout
                    CsvUtil.appendLine("data/logins.csv", DateTimeUtil.format(LocalDateTime.now()) + "," + currentUser.getId() + "," + currentUser.getEmail() + ",LOGOUT");
                    currentUser = null;
                    System.out.println("Logged out successfully!");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
    
    private static int getIntInput() {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return input;
            } catch (Exception e) {
                System.out.print("Invalid input! Please enter a number: ");
                scanner.nextLine(); // clear invalid input
            }
        }
    }
}