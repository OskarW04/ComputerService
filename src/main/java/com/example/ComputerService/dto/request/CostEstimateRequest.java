package com.example.ComputerService.dto.request;

import jakarta.servlet.http.Part;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CostEstimateRequest {
    private String message;
    private List<PartRequest> partRequestList;
    private List<Long> serviceActionIds;
}
