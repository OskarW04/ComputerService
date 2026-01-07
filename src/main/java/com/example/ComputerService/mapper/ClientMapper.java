package com.example.ComputerService.mapper;

import com.example.ComputerService.dto.response.ClientResponse;
import com.example.ComputerService.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public ClientResponse mapToResponse(Client c){
        return new ClientResponse(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getPhone(),
                c.getEmail()
        );
    }
}
