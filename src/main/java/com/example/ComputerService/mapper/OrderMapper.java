package com.example.ComputerService.mapper;

import com.example.ComputerService.dto.response.OrderResponse;
import com.example.ComputerService.model.RepairOrder;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderResponse mapToResponse(RepairOrder order){
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
                order.getManagerNotes()
        );

    }
}
