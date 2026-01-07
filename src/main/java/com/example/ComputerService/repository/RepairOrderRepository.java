package com.example.ComputerService.repository;

import com.example.ComputerService.model.Client;
import com.example.ComputerService.model.Employee;
import com.example.ComputerService.model.RepairOrder;
import com.example.ComputerService.model.enums.RepairOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {
    List<RepairOrder> findAllByStatus(RepairOrderStatus status);
    List<RepairOrder> findByAssignedTechnician(Employee e);
    List<RepairOrder> findByClient(Client c);
}
