package com.example.ComputerService.repository;

import com.example.ComputerService.model.Employee;
import com.example.ComputerService.model.enums.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findAllByRole(EmployeeRole role);
    Optional<Employee> findByEmailAndRole(String email, EmployeeRole role);
}
