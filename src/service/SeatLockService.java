package service;

import model.Seat;
import model.Show;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SeatLockService {
    private final Map<String, Set<String>> lockedSeats; // showId -> locked seat numbers
    private final Map<String, String> seatLocks; // seatKey(showId_seatNumber) -> userId
    private final Map<String, Long> lockTimestamps; // seatKey -> lock timestamp
    private final long lockTimeout = 5 * 60 * 1000; // 5 minutes in milliseconds
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public SeatLockService() {
        this.lockedSeats = new ConcurrentHashMap<>();
        this.seatLocks = new ConcurrentHashMap<>();
        this.lockTimestamps = new ConcurrentHashMap<>();
        
        // Clean up expired locks every minute
        scheduler.scheduleAtFixedRate(this::cleanupExpiredLocks, 1, 1, TimeUnit.MINUTES);
    }
    
    public boolean lockSeats(Show show, List<Seat> seats, String userId) {
        String showId = String.valueOf(show.getId());
        synchronized (this) {
            // Check if any seat is already locked
            for (Seat seat : seats) {
                String seatKey = getSeatKey(showId, String.valueOf(seat.getSeatNumber()));
                if (seatLocks.containsKey(seatKey) && !seatLocks.get(seatKey).equals(userId)) {
                    return false; // Seat already locked by another user
                }
            }
            
            // Lock all seats
            for (Seat seat : seats) {
                String seatNumber = String.valueOf(seat.getSeatNumber());
                String seatKey = getSeatKey(showId, seatNumber);
                
                seatLocks.put(seatKey, userId);
                lockTimestamps.put(seatKey, System.currentTimeMillis());
                lockedSeats.computeIfAbsent(showId, k -> new HashSet<>()).add(seatNumber);
                seat.setStatus(model.SeatStatus.LOCKED);
            }
            return true;
        }
    }
    
    public void unlockSeats(Show show, List<Seat> seats, String userId) {
        String showId = String.valueOf(show.getId());
        synchronized (this) {
            for (Seat seat : seats) {
                String seatNumber = String.valueOf(seat.getSeatNumber());
                String seatKey = getSeatKey(showId, seatNumber);
                
                if (seatLocks.containsKey(seatKey) && seatLocks.get(seatKey).equals(userId)) {
                    seatLocks.remove(seatKey);
                    lockTimestamps.remove(seatKey);
                    Set<String> showLockedSeats = lockedSeats.get(showId);
                    if (showLockedSeats != null) {
                        showLockedSeats.remove(seatNumber);
                        if (showLockedSeats.isEmpty()) {
                            lockedSeats.remove(showId);
                        }
                    }
                    seat.setStatus(model.SeatStatus.AVAILABLE);
                }
            }
        }
    }
    
    public boolean areSeatsLocked(Show show, List<Seat> seats, String userId) {
        String showId = String.valueOf(show.getId());
        for (Seat seat : seats) {
            String seatKey = getSeatKey(showId, String.valueOf(seat.getSeatNumber()));
            String lockingUserId = seatLocks.get(seatKey);
            if (lockingUserId != null && !lockingUserId.equals(userId)) {
                return true;
            }
        }
        return false;
    }
    
    private void cleanupExpiredLocks() {
        long currentTime = System.currentTimeMillis();
        List<String> expiredSeats = new ArrayList<>();
        
        for (Map.Entry<String, Long> entry : lockTimestamps.entrySet()) {
            if (currentTime - entry.getValue() > lockTimeout) {
                expiredSeats.add(entry.getKey());
            }
        }
        
        for (String seatKey : expiredSeats) {
            String[] parts = seatKey.split("_");
            if (parts.length == 2) {
                String showId = parts[0];
                String seatNumber = parts[1];
                
                seatLocks.remove(seatKey);
                lockTimestamps.remove(seatKey);
                Set<String> showLockedSeats = lockedSeats.get(showId);
                if (showLockedSeats != null) {
                    showLockedSeats.remove(seatNumber);
                    if (showLockedSeats.isEmpty()) {
                        lockedSeats.remove(showId);
                    }
                }
            }
        }
    }
    
    private String getSeatKey(String showId, String seatNumber) {
        return showId + "_" + seatNumber;
    }
    
    public void shutdown() {
        scheduler.shutdown();
    }
}