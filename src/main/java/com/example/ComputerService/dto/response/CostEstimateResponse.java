package com.example.ComputerService.dto.response;

import com.example.ComputerService.model.PartUsage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CostEstimateResponse {
    private Long id;
    private Boolean approved;
    private LocalDateTime createdAt;
    private BigDecimal partsCost;
    private BigDecimal labourCost;
    private BigDecimal totalCost;
    private List<PartResponse> parts;
    private List<ActionResponse> actions;
}
