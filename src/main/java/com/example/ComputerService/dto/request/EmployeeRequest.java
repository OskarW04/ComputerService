package com.example.ComputerService.dto.request;

import lombok.Data;

@Data
public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String skillLevel;
}
