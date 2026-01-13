package com.example.ComputerService.mapper;

import com.example.ComputerService.dto.response.PartResponse;
import com.example.ComputerService.model.PartUsage;
import com.example.ComputerService.model.SparePart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartMapper {
    public PartResponse mapToResponse(SparePart part){
        return new PartResponse(
                part.getId(),
                part.getName(),
                part.getType(),
                part.getStockQuantity(),
                part.getPrice()
        );
    }

    public PartResponse mapUsageToResponse(PartUsage usage) {
        SparePart part = usage.getSparePart();
        return new PartResponse(
                part.getId(),
                part.getName(),
                part.getType(),
                usage.getQuantity(),
                usage.getUnitPrice()
        );
    }
}
