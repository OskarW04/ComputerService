package com.example.ComputerService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ActionResponse {
    private Long id;
    private String name;
    private BigDecimal price;
}
