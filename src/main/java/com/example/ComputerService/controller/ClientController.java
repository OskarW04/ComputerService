package com.example.ComputerService.controller;

import com.example.ComputerService.dto.request.PaymentRequest;
import com.example.ComputerService.dto.response.OrderResponse;
import com.example.ComputerService.dto.response.PaymentResponse;
import com.example.ComputerService.model.Client;
import com.example.ComputerService.service.ClientService;
import com.example.ComputerService.service.FinanceService;
import com.example.ComputerService.service.OrderService;
import com.example.ComputerService.service.PaymentService;
import com.example.ComputerService.service.payment.IPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class ClientController {
    private final OrderService orderService;
    private final ClientService clientService;
    private final PaymentService paymentService;
    private final FinanceService financeService;

    @GetMapping("/getClientOrders")
    @PreAuthorize("hasAnyRole('CLIENT', 'MANAGER')")
    public ResponseEntity<List<OrderResponse>> getClientOrders(Authentication auth){
        String phone = auth.getName();
        return ResponseEntity.ok(orderService.getOrdersForClient(phone));
    }

    @GetMapping("/getOrder/{orderId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'MANAGER')")
    public ResponseEntity<OrderResponse> getClientOrder(@PathVariable Long orderId, Authentication auth){
        String phone = auth.getName();
        return ResponseEntity.ok(orderService.getClientOrder(orderId, phone));
    }

    @PutMapping("/accept/{orderId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'MANAGER')")
    public ResponseEntity<String> acceptCostEstimate(@PathVariable Long orderId, Authentication auth){
        String phone = auth.getName();
        return ResponseEntity.ok(clientService.acceptCostEstimate(orderId, phone));
    }

    @PutMapping("/reject/{orderId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'MANAGER')")
    public ResponseEntity<String> rejectCostEstimate(@PathVariable Long orderId, Authentication auth){
        String phone = auth.getName();
        return ResponseEntity.ok(clientService.rejectCostEstimate(orderId, phone));
    }

    @PostMapping("/pay/{orderId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'MANAGER')")
    public ResponseEntity<PaymentResponse> payForMyOrder(
            @RequestBody PaymentRequest request,
            Authentication authentication) {
        String clientEmail = authentication.getName();
        return ResponseEntity.ok(paymentService.registerClientPayment(request, clientEmail));
    }

    @GetMapping("/order/{orderNumber}/generatePdf")
    @PreAuthorize("hasAnyRole('CLIENT', 'MANAGER')")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String orderNumber){
        byte[] pdfBytes = financeService.getDocumentPdf(orderNumber);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=dokument_zlecenia_" + orderNumber.replace("/", "-") + ".pdf")
                .body(pdfBytes);
    }

}
