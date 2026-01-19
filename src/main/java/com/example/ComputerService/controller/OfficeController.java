package com.example.ComputerService.controller;

import com.example.ComputerService.dto.request.ClientRequest;
import com.example.ComputerService.dto.request.DocumentCreationRequest;
import com.example.ComputerService.dto.request.OrderRequest;
import com.example.ComputerService.dto.request.PaymentRequest;
import com.example.ComputerService.dto.response.ClientResponse;
import com.example.ComputerService.dto.response.OrderResponse;
import com.example.ComputerService.dto.response.PaymentResponse;
import com.example.ComputerService.model.BaseDocument;
import com.example.ComputerService.model.Client;
import com.example.ComputerService.model.Payment;
import com.example.ComputerService.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/office")
@RequiredArgsConstructor
public class OfficeController {
    private final ClientService clientService;
    private final OfficeService officeService;
    private final FinanceService financeService;
    private final PaymentService paymentService;

    @PostMapping("/createClient")
    @PreAuthorize("hasAnyRole('OFFICE', 'MANAGER')")
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientRequest request){
        return ResponseEntity.ok(clientService.createClient(request));
    }

    @GetMapping("/getAllClients")
    @PreAuthorize("hasAnyRole('OFFICE', 'MANAGER')")
    public ResponseEntity<List<ClientResponse>> getAllClients(){
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/get/{phone}")
    @PreAuthorize("hasAnyRole('OFFICE', 'MANAGER')")
    public ResponseEntity<ClientResponse> getClient(@PathVariable String phone){
        return ResponseEntity.ok(clientService.getClientByPhone(phone));
    }

    @PutMapping("/acceptEstimateForClient/{orderId}")
    @PreAuthorize("hasAnyRole('OFFICE', 'MANAGER')")
    public ResponseEntity<String> acceptForClient(@PathVariable Long orderId){
        return ResponseEntity.ok(officeService.acceptCostEstimateForClient(orderId));
    }

    @PutMapping("/rejectEstimateForClient/{orderId}")
    @PreAuthorize("hasAnyRole('OFFICE', 'MANAGER')")
    public ResponseEntity<String> rejectForClient(@PathVariable Long orderId){
        return ResponseEntity.ok(officeService.rejectCostEstimateForClient(orderId));
    }

    @PostMapping("/order/createSaleDocument")
    @PreAuthorize("hasAnyRole('OFFICE', 'MANAGER')")
    public ResponseEntity<BaseDocument> createDocument(@RequestBody DocumentCreationRequest request){
        return ResponseEntity.ok(financeService.createSaleDocument(request));
    }

    @GetMapping("/order/{orderNumber}/generatePdf")
    @PreAuthorize("hasAnyRole('OFFICE', 'MANAGER')")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String orderNumber){
        byte[] pdfBytes = financeService.getDocumentPdf(orderNumber);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=dokument_zlecenia_" + orderNumber.replace("/", "-") + ".pdf")
                .body(pdfBytes);
    }

    @PostMapping("/payment")
    @PreAuthorize("hasAnyRole('OFFICE', 'MANAGER')")
    public ResponseEntity<PaymentResponse> registerPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.registerPayment(request));
    }

}
