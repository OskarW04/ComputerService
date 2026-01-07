package com.example.ComputerService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
