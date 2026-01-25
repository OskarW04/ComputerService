package com.example.ComputerService.dto.response;

import com.example.ComputerService.model.PartUsage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MissingPartsResponse {
    private Long orderId;
    private String orderNumber;
    private List<MissingPart> parts;
}
