package com.example.ComputerService.mapper;

import com.example.ComputerService.dto.response.ActionResponse;
import com.example.ComputerService.dto.response.CostEstimateResponse;
import com.example.ComputerService.dto.response.PartResponse;
import com.example.ComputerService.model.CostEstimate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CostEstimateMapper {

    public CostEstimateResponse mapToResponse(CostEstimate est, List<PartResponse> partResponseList, List<ActionResponse> actions){
        return new CostEstimateResponse(
                est.getId(),
                est.getApproved(),
                est.getCreatedAt(),
                est.getPartsCost(),
                est.getLabourCost(),
                est.getTotalCost(),
                partResponseList,
                actions
                );
    }
}
