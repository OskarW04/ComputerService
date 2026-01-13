package com.example.ComputerService.repository;

import com.example.ComputerService.model.Invoice;
import com.example.ComputerService.model.Payment;
import com.example.ComputerService.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByInvoice(Invoice invoice);
    List<Payment> findByReceipt(Receipt receipt);
}
