package com.example.ComputerService.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MfApiResponse {
    private MfResult result;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MfResult {
        private MfSubject subject;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MfSubject {
        private String name;
        private String workingAddress;
        private String residenceAddress;
        private String statusVat;
    }
}
