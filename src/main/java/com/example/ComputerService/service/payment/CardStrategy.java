package com.example.ComputerService.service.payment;

import com.example.ComputerService.model.Payment;
import com.example.ComputerService.model.enums.PaymentMethod;
import com.example.ComputerService.model.enums.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class CardStrategy implements IPaymentStrategy{
    @Override
    public void process(Payment payment) {
        // Simulating payment terminal
        boolean terminalSuccess = true;

        if (terminalSuccess) {
            payment.setStatus(PaymentStatus.ACCEPTED);
        } else {
            payment.setStatus(PaymentStatus.REJECTED);
            throw new RuntimeException("Payment rejected");
        }
    }

    @Override
    public PaymentMethod getMethod() { return PaymentMethod.CARD; }
}
