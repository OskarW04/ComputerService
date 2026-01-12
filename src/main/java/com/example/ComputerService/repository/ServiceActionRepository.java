package com.example.ComputerService.repository;

import com.example.ComputerService.model.ServiceAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceActionRepository extends JpaRepository<ServiceAction, Long> {
}
