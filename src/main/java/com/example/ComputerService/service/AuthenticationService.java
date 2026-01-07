package com.example.ComputerService.service;

import com.example.ComputerService.dto.request.LoginRequest;
import com.example.ComputerService.dto.response.AuthResponse;
import com.example.ComputerService.dto.response.PinGenResponse;
import com.example.ComputerService.model.Client;
import com.example.ComputerService.repository.ClientRepository;
import com.example.ComputerService.repository.EmployeeRepository;
import com.example.ComputerService.security.CustomUserDetailsService;
import com.example.ComputerService.security.JwtService;
import com.example.ComputerService.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final ClientService clientService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse authenticate(LoginRequest request) {
        String identifier = request.getEmail();
        String secret = request.getPassword();

        // check if employee
        var employeeOpt = employeeRepository.findByEmail(identifier);

        if (employeeOpt.isPresent()) {
            var employee = employeeOpt.get();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(identifier, secret)
            );

            var jwtToken = jwtService.generateToken(new SecurityUser(employee));

            return AuthResponse.builder()
                    .token(jwtToken)
                    .role(employee.getRole().name())
                    .username(employee.getEmail())
                    .build();
        }

        // check if client
        var clientOpt = clientRepository.findByPhone(identifier);

        if (clientOpt.isPresent()) {
            var client = clientService.verifyPIN(identifier, secret);
            var jwtToken = jwtService.generateToken(new SecurityUser(client));

            return AuthResponse.builder()
                    .token(jwtToken)
                    .role("CLIENT")
                    .username(client.getPhone())
                    .build();
        }

        throw new RuntimeException("Nieprawidłowy login lub hasło/pin");
    }

    @Transactional
    public PinGenResponse generatePin(String phone){
        Client client = clientRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Client with that phone number doesnt exists"));

        String pin = String.valueOf((int)(Math.random()*9000)+1000);
        client.setPin(pin);

        clientRepository.save(client);

        return new PinGenResponse(
                pin,
                "PIN code sent successfully"
        );
    }
}
