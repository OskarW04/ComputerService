package com.example.ComputerService.controller;

import com.example.ComputerService.dto.request.ClientRequest;
import com.example.ComputerService.dto.request.OrderRequest;
import com.example.ComputerService.dto.response.ClientResponse;
import com.example.ComputerService.dto.response.OrderResponse;
import com.example.ComputerService.model.Client;
import com.example.ComputerService.service.ClientService;
import com.example.ComputerService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/office")
@RequiredArgsConstructor
public class OfficeController {
    private final ClientService clientService;

    @PostMapping("/createClient")
    @PreAuthorize("hasAnyRole('OFFICE', 'MANAGER')")
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientRequest request){
        return ResponseEntity.ok(clientService.createClient(request));
    }

    @GetMapping("/getAllClients")
    @PreAuthorize("hasRole('OFFICE')")
    public ResponseEntity<List<ClientResponse>> getAllClients(){
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/get/{phone}")
    @PreAuthorize("hasRole('OFFICE')")
    public ResponseEntity<ClientResponse> getClient(@PathVariable String phone){
        return ResponseEntity.ok(clientService.getClientByPhone(phone));
    }

}
