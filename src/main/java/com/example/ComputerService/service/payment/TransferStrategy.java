package com.example.ComputerService.service.payment;

import com.example.ComputerService.model.Payment;
import com.example.ComputerService.model.enums.PaymentMethod;
import com.example.ComputerService.model.enums.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class TransferStrategy implements IPaymentStrategy{
    @Override
    public void process(Payment payment) {
        // Have to wait to be completed
        payment.setStatus(PaymentStatus.PENDING_SYNC);
    }

    @Override
    public PaymentMethod getMethod() { return PaymentMethod.BANK_TRANSFER; }
}
