package com.example.ComputerService.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class ClientRequest {
    @NotBlank(message = "Name is required")
    private String firstName;

    @NotBlank(message = "Surname is required")
    private String lastName;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String email;
}
