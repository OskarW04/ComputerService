package com.example.ComputerService.dto.request;

import lombok.Data;

@Data
public class DocumentCreationRequest {
    private Long orderId;
    private String docType;
    private String nip; // optional for invoice
}
