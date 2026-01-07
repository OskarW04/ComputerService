package com.example.ComputerService.service;

import com.example.ComputerService.dto.request.ClientRequest;
import com.example.ComputerService.dto.response.ClientResponse;
import com.example.ComputerService.dto.response.OrderResponse;
import com.example.ComputerService.mapper.ClientMapper;
import com.example.ComputerService.model.Client;
import com.example.ComputerService.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientResponse createClient(ClientRequest request){
        if(clientRepository.findByPhone(request.getPhone()).isPresent()){
            throw new RuntimeException("Client with that phone number already exists");
        }

        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPhone(request.getPhone());
        client.setEmail(request.getEmail());

        client.setPin(null);

        Client newClient = clientRepository.save(client);
        return clientMapper.mapToResponse(newClient);
    }

    @Transactional
    public Client verifyPIN(String phone, String pin){
        Client client = clientRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Client with this phone number doesnt exist"));

        if (client.getPin() == null) {
            throw new RuntimeException("Generate new PIN");
        }

        if (!client.getPin().equals(pin)) {
            throw new RuntimeException("Provided wrong PIN");
        }

        client.setPin(null);
        clientRepository.save(client);

        return client;
    }

    public List<ClientResponse> getAllClients(){
         return clientRepository.findAll().stream()
                 .map(clientMapper::mapToResponse)
                 .collect(Collectors.toList());
    }

    public ClientResponse getClientByPhone(String phone){
        Client client = clientRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Client does not exist"));
        return clientMapper.mapToResponse(client);
    }


}
