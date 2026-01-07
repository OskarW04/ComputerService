package com.example.ComputerService.service;

import com.example.ComputerService.dto.request.CostEstimateRequest;
import com.example.ComputerService.dto.response.CostEstimateResponse;
import com.example.ComputerService.dto.response.OrderResponse;
import com.example.ComputerService.mapper.CostEstimateMapper;
import com.example.ComputerService.mapper.OrderMapper;
import com.example.ComputerService.model.CostEstimate;
import com.example.ComputerService.model.Employee;
import com.example.ComputerService.model.RepairOrder;
import com.example.ComputerService.model.enums.EmployeeRole;
import com.example.ComputerService.model.enums.RepairOrderStatus;
import com.example.ComputerService.repository.EmployeeRepository;
import com.example.ComputerService.repository.RepairOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TechnicianService {
    private final EmployeeRepository employeeRepository;
    private final RepairOrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CostEstimateMapper costEstimateMapper;

    @Transactional
    public void startDiagnosing(Long orderId, String technicianEmail){
        Employee e = employeeRepository.findByEmailAndRole(technicianEmail, EmployeeRole.TECHNICIAN)
                .orElseThrow(() -> new RuntimeException("Cant find Technician with that id number"));

        RepairOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order with that id does not exist"));

        if(order.getAssignedTechnician() != e){
            throw new RuntimeException("This order is not assigned to this technician");
        }

        if(order.getStatus() != RepairOrderStatus.NEW && order.getStatus() != RepairOrderStatus.WAITING_FOR_TECHNICIAN){
            throw new RuntimeException("Status of this order cant be changed");
        }
        order.setStartDate(LocalDateTime.now());
        order.setStatus(RepairOrderStatus.DIAGNOSING);
        orderRepository.save(order);
    }

    @Transactional
    public CostEstimateResponse generateCostEst(Long orderId, CostEstimateRequest request, String techEmail){
        RepairOrder o = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order with that id does not exist"));
        if(!Objects.equals(o.getAssignedTechnician().getEmail(), techEmail)){
            throw new RuntimeException("This order is not assigned to this technician");
        }
        String currNote = o.getManagerNotes() != null ? o.getManagerNotes()+"\n" : "";
        o.setManagerNotes(currNote + "[DIAGNOSIS] " + request.getMessage());
        CostEstimate estimate = new CostEstimate();
        estimate.setApproved(null);
        BigDecimal parts = request.getPartsCost();
        BigDecimal labour = request.getLabourCost();
        estimate.setLabourCost(labour);
        estimate.setPartsCost(parts);
        estimate.setTotalCost(parts.add(labour));
        estimate.setCreatedAt(LocalDateTime.now());
        estimate.setRepairOrder(o);
        OrderResponse orderResp = orderMapper.mapToResponse(o);

        return costEstimateMapper.mapToResponse(estimate, orderResp);
    }



}
