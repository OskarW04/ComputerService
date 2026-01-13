package com.example.ComputerService.model;

import com.example.ComputerService.model.enums.PaymentMethod;
import com.example.ComputerService.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime paymentDate;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    public Payment(BigDecimal amount, PaymentMethod method) {
        this.amount = amount;
        this.method = method;
        this.paymentDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING_SYNC;
    }
}
