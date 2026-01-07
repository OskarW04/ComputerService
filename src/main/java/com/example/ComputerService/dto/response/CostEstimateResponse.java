package com.example.ComputerService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CostEstimateResponse {
    private Boolean approved;
    private LocalDateTime createdAt;
    private BigDecimal partsCost;
    private BigDecimal labourCost;
    private BigDecimal totalCost;
    private OrderResponse repairOrder;
}
