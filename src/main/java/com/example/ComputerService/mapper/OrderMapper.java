package com.example.ComputerService.mapper;

import com.example.ComputerService.dto.response.CostEstimateResponse;
import com.example.ComputerService.dto.response.OrderResponse;
import com.example.ComputerService.dto.response.PartResponse;
import com.example.ComputerService.model.CostEstimate;
import com.example.ComputerService.model.PartUsage;
import com.example.ComputerService.model.RepairOrder;
import com.example.ComputerService.model.SparePart;
import com.example.ComputerService.repository.CostEstRepository;
import com.example.ComputerService.repository.PartUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final CostEstRepository costEstRepository;
    private final CostEstimateMapper costEstimateMapper;
    private final PartUsageRepository partUsageRepository;
    private final PartMapper partMapper;

    public OrderResponse mapToResponse(RepairOrder order){
        CostEstimate estimate = costEstRepository.findByRepairOrder(order)
                .orElse(null);
        CostEstimateResponse estResponse = null;
        if (estimate != null) {
            List<PartUsage> usages = partUsageRepository.findByRepairOrder(order);
            List<PartResponse> partResponses = usages.stream()
                    .map(partMapper::mapUsageToResponse)
                    .toList();
            estResponse = costEstimateMapper.mapToResponse(estimate, partResponses);
        }

        return mapToResponse(order, estResponse);
    }

    public OrderResponse mapToResponse(RepairOrder order, CostEstimateResponse est){
        // technician may be null if order is new
        String techName = null;
        if(order.getAssignedTechnician() != null){
            techName = order.getAssignedTechnician().getFirstName() +
                    " " + order.getAssignedTechnician().getLastName();
        }

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCreatedAt(),
                order.getStartDate(),
                order.getEndDate(),
                order.getDeviceDescription(),
                order.getProblemDescription(),
                order.getStatus().name(),
                order.getClient().getId(),
                order.getClient().getFirstName() + " " + order.getClient().getLastName(),
                order.getClient().getPhone(),
                techName,
                order.getManagerNotes(),
                est
        );

    }
}
