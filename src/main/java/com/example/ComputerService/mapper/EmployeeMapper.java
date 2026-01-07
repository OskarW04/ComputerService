package com.example.ComputerService.mapper;

import com.example.ComputerService.dto.response.EmployeeResponse;
import com.example.ComputerService.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public EmployeeResponse mapToResponse(Employee e) {
        return new EmployeeResponse(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getEmail(),
                e.getRole().name(),
                e.getSkillLevel() != null ? e.getSkillLevel().name() : null
        );
    }
}
