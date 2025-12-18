package service;

import model.Booking;
import model.PaymentMethod;

public class PaymentService {
    
    public boolean processPayment(Booking booking, PaymentMethod paymentMethod) {
        // Simulate payment processing
        try {
            Thread.sleep(1000); // Simulate API call
            // In real implementation, integrate with payment gateway
            System.out.println("Processing payment of " + booking.getTotalAmount() + 
                             " for booking " + booking.getId());
            return Math.random() > 0.1; // 90% success rate for simulation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    public void processRefund(Booking booking) {
        // Simulate refund processing
        System.out.println("Processing refund for booking " + booking.getId());
        try {
            Thread.sleep(500); // simulate refund delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Refund processed for booking " + booking.getId());
    }

}
