package com.example.ComputerService.mapper;

import com.example.ComputerService.dto.response.ActionResponse;
import com.example.ComputerService.model.ActionUsage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionMapper {
    public ActionResponse mapToResponse(ActionUsage usage){
        return new ActionResponse(
                usage.getId(),
                usage.getServiceAction().getName(),
                usage.getCurrentPrice()
        );
    }
}
