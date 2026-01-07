package com.example.ComputerService.controller;

import com.example.ComputerService.dto.response.OrderResponse;
import com.example.ComputerService.service.ClientService;
import com.example.ComputerService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class ClientController {
    private final OrderService orderService;

    @GetMapping("/getClientOrders")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<OrderResponse>> getClientOrders(Authentication auth){
        String phone = auth.getName();
        return ResponseEntity.ok(orderService.getOrdersForClient(phone));
    }

    @GetMapping("getOrder/{orderId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OrderResponse> getClientOrder(@PathVariable Long orderId, Authentication auth){
        String phone = auth.getName();
        return ResponseEntity.ok(orderService.getClientOrder(orderId, phone));
    }

}
