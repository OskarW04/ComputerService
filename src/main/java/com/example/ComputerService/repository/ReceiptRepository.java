package com.example.ComputerService.repository;

import com.example.ComputerService.model.Receipt;
import com.example.ComputerService.model.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByRepairOrderId(Long orderId);
    Optional<Receipt> findByRepairOrderOrderNumber(String number);
    Boolean existsByRepairOrder(RepairOrder order);
}
