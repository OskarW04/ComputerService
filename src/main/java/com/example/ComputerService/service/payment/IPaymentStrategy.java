package com.example.ComputerService.service.payment;

import com.example.ComputerService.model.Payment;
import com.example.ComputerService.model.enums.PaymentMethod;

public interface IPaymentStrategy {
    void process(Payment payment);
    PaymentMethod getMethod();
}
