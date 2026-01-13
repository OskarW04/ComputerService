package com.example.ComputerService.model;

import com.example.ComputerService.model.enums.SaleDocumentStatus;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseDocument {
    protected String documentNumber;
    protected LocalDateTime issueDate;
    protected BigDecimal totalAmount;
    protected SaleDocumentStatus status;

    @OneToOne
    @JoinColumn(name = "order_id")
    protected RepairOrder repairOrder;
}
