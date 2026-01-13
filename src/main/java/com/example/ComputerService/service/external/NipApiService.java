package com.example.ComputerService.service.external;

import com.example.ComputerService.dto.external.MfApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NipApiService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://wl-api.mf.gov.pl/api/search/nip/%s?date=%s";
    public MfApiResponse.MfSubject getCompanyData(String nip) {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String cleanNip = nip.replace("-", "").trim();
        String url = String.format(API_URL, cleanNip, date);
        try {
            MfApiResponse response = restTemplate.getForObject(url, MfApiResponse.class);
            if (response != null && response.getResult() != null && response.getResult().getSubject() != null) {
                return response.getResult().getSubject();
            }
        } catch (Exception e) {
            System.err.println("Error connecting to API: " + e.getMessage());
        }
        return null;
    }
}
