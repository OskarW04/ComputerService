package com.example.ComputerService.service.payment;

import com.example.ComputerService.model.Payment;
import com.example.ComputerService.model.enums.PaymentMethod;
import com.example.ComputerService.model.enums.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class CashStrategy implements IPaymentStrategy{
    @Override
    public void process(Payment payment) {
        payment.setStatus(PaymentStatus.ACCEPTED);
    }

    @Override
    public PaymentMethod getMethod() { return PaymentMethod.CASH; }
}
