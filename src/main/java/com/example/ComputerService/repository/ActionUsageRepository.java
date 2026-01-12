package com.example.ComputerService.repository;

import com.example.ComputerService.model.ActionUsage;
import com.example.ComputerService.model.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionUsageRepository extends JpaRepository<ActionUsage, Long> {
    List<ActionUsage> findByRepairOrder(RepairOrder repairOrder);
}
