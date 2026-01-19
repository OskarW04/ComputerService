package com.example.ComputerService.controller;

import com.example.ComputerService.dto.request.LoginRequest;
import com.example.ComputerService.dto.request.PinGenRequest;
import com.example.ComputerService.dto.response.AuthResponse;
import com.example.ComputerService.dto.response.ClientResponse;
import com.example.ComputerService.dto.response.EmployeeResponse;
import com.example.ComputerService.dto.response.PinGenResponse;
import com.example.ComputerService.service.AuthenticationService;
import com.example.ComputerService.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/generatePIN")
    public ResponseEntity<PinGenResponse> generatePIN(@RequestBody PinGenRequest request){
        return ResponseEntity.ok(authService.generatePin(request.getPhone()));
    }

    @GetMapping("/getMe")
    public ResponseEntity<EmployeeResponse> getMe(Authentication auth){
        String email = auth.getName();
        return ResponseEntity.ok(authService.getMe(email));
    }


}
