package com.example.ComputerService.controller;

import com.example.ComputerService.dto.request.OrderRequest;
import com.example.ComputerService.dto.response.OrderResponse;
import com.example.ComputerService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/createOrder")
    @PreAuthorize("hasAnyRole('OFFICE', 'MANAGER')")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request){
        return ResponseEntity.ok(orderService.createOrder(request));
    }


    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('OFFICE', 'MANAGER')")
    public ResponseEntity<List<OrderResponse>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/getAllNew")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<OrderResponse>> getAllNewOrders(){
        return ResponseEntity.ok(orderService.getAllNewOrders());
    }


}
