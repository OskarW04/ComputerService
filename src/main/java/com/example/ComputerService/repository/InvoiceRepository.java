package com.example.ComputerService.repository;

import com.example.ComputerService.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByRepairOrderId(Long orderId);
    Optional<Invoice> findByRepairOrderOrderNumber(String number);
}
