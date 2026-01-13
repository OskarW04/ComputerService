package com.example.ComputerService.service;

import com.example.ComputerService.dto.request.DocumentCreationRequest;
import com.example.ComputerService.model.BaseDocument;
import com.example.ComputerService.model.Invoice;
import com.example.ComputerService.model.Receipt;
import com.example.ComputerService.model.RepairOrder;
import com.example.ComputerService.model.enums.RepairOrderStatus;
import com.example.ComputerService.model.enums.SaleDocumentStatus;
import com.example.ComputerService.repository.InvoiceRepository;
import com.example.ComputerService.repository.ReceiptRepository;
import com.example.ComputerService.repository.RepairOrderRepository;
import com.example.ComputerService.service.sale_document.DocumentFactory;
import com.example.ComputerService.service.sale_document.IDocumentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FinanceService {
    private final RepairOrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final ReceiptRepository receiptRepository;
    private final DocumentFactory documentFactory;

    @Transactional
    public BaseDocument createSaleDocument(DocumentCreationRequest request){
        RepairOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Can't find order with that ID"));
        if (order.getStatus() != RepairOrderStatus.READY_FOR_PICKUP) {
            throw new RuntimeException("Order is not ready for pickup yet");
        }

        IDocumentStrategy strategy = documentFactory.getStrategy(request.getDocType());
        return strategy.createDocument(order, request);
    }

    @Transactional(readOnly = true)
    public byte[] getDocumentPdf(String orderNum) {
        Optional<Invoice> invoice = invoiceRepository.findByRepairOrderOrderNumber(orderNum);
        if (invoice.isPresent()) {
            IDocumentStrategy strategy = documentFactory.getStrategy("INVOICE");
            return strategy.generatePdf(invoice.get());
        }
        Optional<Receipt> receipt = receiptRepository.findByRepairOrderOrderNumber(orderNum);
        if (receipt.isPresent()) {
            IDocumentStrategy strategy = documentFactory.getStrategy("RECEIPT");
            return strategy.generatePdf(receipt.get());
        }

        throw new RuntimeException("For order with ID " + orderNum + " there is not assigned sale document");

    }
}
