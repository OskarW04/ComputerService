package com.example.ComputerService.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CostEstimateRequest {
    private String message;
    private BigDecimal partsCost;
    private BigDecimal labourCost;

}
