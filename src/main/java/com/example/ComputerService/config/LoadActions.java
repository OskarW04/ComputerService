package com.example.ComputerService.config;


import com.example.ComputerService.model.ServiceAction;
import com.example.ComputerService.repository.ServiceActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoadActions implements CommandLineRunner {
    private final ServiceActionRepository serviceActionRepository;
    @Override
    public void run(String ... args){
        if (serviceActionRepository.count() == 0) {
                serviceActionRepository.saveAll(List.of(
                        // Diagnostics
                        new ServiceAction(null, "Diagnoza podstawowa", new BigDecimal("50.00")),

                        // Software
                        new ServiceAction(null, "Instalacja systemu Windows + Sterowniki", new BigDecimal("150.00")),
                        new ServiceAction(null, "Usuwanie wirusów i złośliwego oprogramowania", new BigDecimal("100.00")),
                        new ServiceAction(null, "Kopia zapasowa danych / Archiwizacja", new BigDecimal("80.00")),
                        new ServiceAction(null, "Odzyskiwanie danych", new BigDecimal("250.00")),

                        // Conservation
                        new ServiceAction(null, "Czyszczenie układu chłodzenia + wymiana past (Laptop)", new BigDecimal("120.00")),
                        new ServiceAction(null, "Czyszczenie wnętrza obudowy (Komputer PC)", new BigDecimal("70.00")),

                        // Hardware
                        new ServiceAction(null, "Demontaż", new BigDecimal("80.00")),
                        new ServiceAction(null, "Montaż", new BigDecimal("80.00")),
                        new ServiceAction(null, "Wymiana matrycy LCD w laptopie (robocizna)", new BigDecimal("100.00")),                        new ServiceAction(null, "Wymiana matrycy LCD w laptopie (robocizna)", new BigDecimal("100.00")),
                        new ServiceAction(null, "Wymiana gniazda zasilania (lutowanie)", new BigDecimal("140.00")),
                        new ServiceAction(null, "Montaż dysku SSD + klonowanie systemu", new BigDecimal("100.00")),
                        new ServiceAction(null, "Składanie komputera PC z części", new BigDecimal("200.00"))
                ));

            System.out.println("Initialized service action database");
        }
    }
}
