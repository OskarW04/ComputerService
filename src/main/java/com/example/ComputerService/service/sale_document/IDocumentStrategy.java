package com.example.ComputerService.service.sale_document;

import com.example.ComputerService.dto.request.DocumentCreationRequest;
import com.example.ComputerService.model.BaseDocument;
import com.example.ComputerService.model.RepairOrder;

public interface IDocumentStrategy {
    BaseDocument createDocument(RepairOrder order, DocumentCreationRequest request);
    byte[] generatePdf(BaseDocument document);
    BaseDocument findById(Long id);
    String getType();
}
