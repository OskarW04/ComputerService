package com.example.ComputerService.dto.response;

import com.example.ComputerService.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long id;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private PaymentStatus status;
}
