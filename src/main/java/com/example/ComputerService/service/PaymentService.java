package com.example.ComputerService.service;

import com.example.ComputerService.dto.request.PaymentRequest;
import com.example.ComputerService.dto.response.PaymentResponse;
import com.example.ComputerService.mapper.PaymentMapper;
import com.example.ComputerService.model.*;
import com.example.ComputerService.model.enums.PaymentStatus;
import com.example.ComputerService.model.enums.RepairOrderStatus;
import com.example.ComputerService.model.enums.SaleDocumentStatus;
import com.example.ComputerService.repository.InvoiceRepository;
import com.example.ComputerService.repository.PaymentRepository;
import com.example.ComputerService.repository.ReceiptRepository;
import com.example.ComputerService.repository.RepairOrderRepository;
import com.example.ComputerService.service.payment.PaymentFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final InvoiceRepository invoiceRepository;
    private final ReceiptRepository receiptRepository;
    private final PaymentRepository paymentRepository;
    private final RepairOrderRepository orderRepository;
    private final PaymentFactory paymentFactory;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentResponse registerPayment(PaymentRequest request) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findByRepairOrderId(request.getOrderId());
        Optional<Receipt> receiptOpt = receiptRepository.findByRepairOrderId(request.getOrderId());

        if (invoiceOpt.isEmpty() && receiptOpt.isEmpty()) {
            throw new RuntimeException("There is not assigned sale document to this order");
        }

        Payment payment = new Payment(request.getAmount(), request.getMethod());

        BaseDocument document;
        if (invoiceOpt.isPresent()) {
            document = invoiceOpt.get();
            payment.setInvoice((Invoice) document);
        } else {
            document = receiptOpt.get();
            payment.setReceipt((Receipt) document);
        }

        paymentFactory.getStrategy(request.getMethod()).process(payment);
        paymentRepository.save(payment);

        if (payment.getStatus() == PaymentStatus.ACCEPTED) {
            checkIfFullyPaid(document, invoiceOpt.isPresent());
        }
        return paymentMapper.mapToResponse(payment);
    }

    private void checkIfFullyPaid(BaseDocument document, boolean isInvoice) {
        List<Payment> payments;
        if (isInvoice) {
            payments = paymentRepository.findByInvoice((Invoice) document);
        } else {
            payments = paymentRepository.findByReceipt((Receipt) document);
        }

        BigDecimal totalPaid = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.ACCEPTED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPaid.compareTo(document.getTotalAmount()) >= 0) {

            document.setStatus(SaleDocumentStatus.PAID);
            if (isInvoice) invoiceRepository.save((Invoice) document);
            else receiptRepository.save((Receipt) document);

            RepairOrder order = document.getRepairOrder();
            order.setStatus(RepairOrderStatus.COMPLETED);
            orderRepository.save(order);
        }
    }

    @Transactional
    public PaymentResponse registerClientPayment(PaymentRequest request, String clientEmail) {
        RepairOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order doesn't exist"));

        if (!order.getClient().getEmail().equals(clientEmail)) {
            throw new RuntimeException("This order doesn't belong to you");
        }
        return registerPayment(request);
    }
}
