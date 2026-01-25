package com.example.ComputerService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MissingPart {
    private Long id;
    private String name;
    private String type;
    private Integer currentCount;
    private Integer neededCount;
    private BigDecimal price;
}
