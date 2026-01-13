package com.example.ComputerService.dto.request;

import com.example.ComputerService.model.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long orderId;
    private BigDecimal amount;
    private PaymentMethod method;
}
