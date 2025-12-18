package repository;

import model.Booking;
import model.BookingStatus;
import model.Seat;
import util.CsvUtil;
import util.DateTimeUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class BookingRepository {
    private Map<String, Booking> bookings;
    private Map<String, List<Booking>> userBookings; // userId -> list of bookings
    private final String CSV_PATH = "data/bookings.csv";
    private ShowRepository showRepository;
    private UserRepository userRepository;

    public BookingRepository(ShowRepository showRepository, UserRepository userRepository) {
        this.bookings = new HashMap<>();
        this.userBookings = new HashMap<>();
        this.showRepository = showRepository;
        this.userRepository = userRepository;
        // ensure CSV has header and load existing bookings
        CsvUtil.ensureHeaderExists(CSV_PATH, "bookingId,userId,userEmail,showId,seats,totalAmount,status,bookingTime,expiryTime");
        loadFromCsv();
    }

    public void addBooking(Booking booking) {
        String bookingId = booking.getId();
        String userId = booking.getUser().getId();

        bookings.put(bookingId, booking);
        userBookings.computeIfAbsent(userId, k -> new ArrayList<>()).add(booking);

        saveAllToCsv();
    }

    public Booking getBooking(String id) {
        return bookings.get(id);
    }

    public List<Booking> getUserBookings(String userId) {
        return userBookings.getOrDefault(userId, new ArrayList<>());
    }

    public void updateBooking(Booking booking) {
        bookings.put(booking.getId(), booking);
        saveAllToCsv();
    }

    private void saveAllToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("bookingId,userId,userEmail,showId,seats,totalAmount,status,bookingTime,expiryTime");
        for (Booking b : bookings.values()) {
            String seats = b.getSeats().stream()
                    .map(Seat::getSeatNumber)
                    .collect(Collectors.joining(";"));
            StringBuilder sb = new StringBuilder();
            sb.append(CsvUtil.escape(b.getId())).append(",");
            sb.append(CsvUtil.escape(b.getUser().getId())).append(",");
            sb.append(CsvUtil.escape(b.getUser().getEmail())).append(",");
            sb.append(CsvUtil.escape(b.getShow().getId())).append(",");
            sb.append(CsvUtil.escape(seats)).append(",");
            sb.append(b.getTotalAmount()).append(",");
            sb.append(CsvUtil.escape(b.getStatus().name())).append(",");
            sb.append(CsvUtil.escape(DateTimeUtil.format(b.getBookingTime()))).append(",");
            sb.append(CsvUtil.escape(DateTimeUtil.format(b.getExpiryTime())));
            lines.add(sb.toString());
        }
        CsvUtil.overwriteLines(CSV_PATH, lines);
    }

    private void loadFromCsv() {
        Path p = Paths.get(CSV_PATH);
        if (!Files.exists(p)) return;
        try {
            List<String> lines = Files.readAllLines(p);
            boolean first = true;
            for (String line : lines) {
                if (first) { first = false; continue; }
                if (line.trim().isEmpty()) continue;
                // naive CSV split - assumes no embedded commas in fields except quoted
                String[] parts = line.split(",");
                if (parts.length < 9) continue;
                String bookingId = parts[0].replaceAll("^\"|\"$", "");
                String userId = parts[1].replaceAll("^\"|\"$", "");
                String showId = parts[3].replaceAll("^\"|\"$", "");
                String seatsField = parts[4].replaceAll("^\"|\"$", "");
                String totalAmountStr = parts[5];
                String statusStr = parts[6].replaceAll("^\"|\"$", "");
                String bookingTimeStr = parts[7].replaceAll("^\"|\"$", "");
                String expiryTimeStr = parts[8].replaceAll("^\"|\"$", "");

                // lookup user and show
                model.User user = userRepository.getUser(userId);
                model.Show show = showRepository.getShow(showId);
                if (user == null || show == null) continue; // skip if references missing

                List<String> seatNums = Arrays.asList(seatsField.split(";"));
                List<Seat> seats = show.getScreen().getSeats().stream()
                        .filter(s -> seatNums.contains(s.getSeatNumber()))
                        .collect(Collectors.toList());

                double totalAmount = 0.0;
                try { totalAmount = Double.parseDouble(totalAmountStr); } catch (Exception ex) {}
                Booking b = new Booking(bookingId, user, show, seats, totalAmount,
                    BookingStatus.valueOf(statusStr),
                    DateTimeUtil.parse(bookingTimeStr), DateTimeUtil.parse(expiryTimeStr));

                bookings.put(bookingId, b);
                userBookings.computeIfAbsent(userId, k -> new ArrayList<>()).add(b);
            }
        } catch (IOException e) {
            System.err.println("Failed to load bookings from CSV: " + e.getMessage());
        }
    }
}