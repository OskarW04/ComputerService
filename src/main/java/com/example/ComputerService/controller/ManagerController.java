package com.example.ComputerService.controller;

import com.example.ComputerService.dto.response.OrderResponse;
import com.example.ComputerService.repository.RepairOrderRepository;
import com.example.ComputerService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final OrderService orderService;

    @PutMapping("/{orderId}/assign/{technicianId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<OrderResponse> assignTechnician(@PathVariable Long orderId, @PathVariable Long technicianId) {
        return ResponseEntity.ok(orderService.assignTechnician(orderId, technicianId));
    }

}
