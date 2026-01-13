package com.example.ComputerService.mapper;

import com.example.ComputerService.dto.response.PaymentResponse;
import com.example.ComputerService.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {
    public PaymentResponse mapToResponse(Payment payment){
        return new PaymentResponse(
                payment.getId(),
                payment.getPaymentDate(),
                payment.getAmount(),
                payment.getStatus()
        );
    }
}
