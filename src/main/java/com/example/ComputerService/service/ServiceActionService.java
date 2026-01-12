package com.example.ComputerService.service;

import com.example.ComputerService.model.ServiceAction;
import com.example.ComputerService.repository.ServiceActionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceActionService {
    private final ServiceActionRepository serviceActionRepository;

    public List<ServiceAction> getAllServices(){
        return serviceActionRepository.findAll();
    }

    public ServiceAction addService(ServiceAction service){
        return serviceActionRepository.save(service);
    }

    public ServiceAction updateService(Long id, ServiceAction toEdit){
        ServiceAction edited = serviceActionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can't find service with ID " + id));
        edited.setName(toEdit.getName());
        edited.setPrice(toEdit.getPrice());
        return edited;
    }

    public String deleteService(Long id){
        ServiceAction toDelete = serviceActionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can't find service with ID " + id));
        serviceActionRepository.delete(toDelete);
        return "Successfully deleted service action with id: " + id;
    }

}
