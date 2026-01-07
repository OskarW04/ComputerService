package com.example.ComputerService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotBlank(message = "Device description is required")
    private String deviceDescription;

    @NotBlank(message = "Problem description is required")
    private String problemDescription;
}
