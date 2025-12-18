package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvCleaner {
    public static void main(String[] args) {
        cleanUsers();
        cleanBookings();
        System.out.println("CSV cleanup completed.");
    }

    private static void cleanUsers() {
        Path p = Paths.get("data/users.csv");
        if (!Files.exists(p)) return;
        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            if (lines.isEmpty()) return;
            String header = "userId,name,email,phone";
            Map<String, String> byEmail = new LinkedHashMap<>();
            for (String line : lines) {
                if (line == null) continue;
                String t = line.trim();
                if (t.isEmpty()) continue;
                if (t.equals(header)) continue;
                String[] parts = line.split(",", 4);
                if (parts.length < 4) continue;
                String email = parts[2].replaceAll("^\"|\"$", "").toLowerCase();
                // keep first occurrence
                byEmail.putIfAbsent(email, parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3]);
            }
            List<String> out = new java.util.ArrayList<>();
            out.add(header);
            out.addAll(byEmail.values());
            Files.write(p, out, StandardCharsets.UTF_8);
            System.out.println("Cleaned users.csv: kept " + byEmail.size() + " unique users.");
        } catch (IOException e) {
            System.err.println("Failed to clean users.csv: " + e.getMessage());
        }
    }

    private static void cleanBookings() {
        Path p = Paths.get("data/bookings.csv");
        if (!Files.exists(p)) return;
        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            if (lines.isEmpty()) return;
            String header = "bookingId,userId,userEmail,showId,seats,totalAmount,status,bookingTime,expiryTime";
            Map<String, String> byId = new LinkedHashMap<>();
            for (String line : lines) {
                if (line == null) continue;
                String t = line.trim();
                if (t.isEmpty()) continue;
                if (t.equals(header)) continue;
                String[] parts = line.split(",", 9);
                if (parts.length < 1) continue;
                String id = parts[0].replaceAll("^\"|\"$", "");
                byId.putIfAbsent(id, line);
            }
            List<String> out = new java.util.ArrayList<>();
            out.add(header);
            out.addAll(byId.values());
            Files.write(p, out, StandardCharsets.UTF_8);
            System.out.println("Cleaned bookings.csv: kept " + byId.size() + " unique bookings.");
        } catch (IOException e) {
            System.err.println("Failed to clean bookings.csv: " + e.getMessage());
        }
    }
}
