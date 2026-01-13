package com.example.ComputerService.service.sale_document;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DocumentFactory {
    private final Map<String, IDocumentStrategy> strategies;
    public DocumentFactory(List<IDocumentStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(IDocumentStrategy::getType, Function.identity()));
    }

    public IDocumentStrategy getStrategy(String type) {
        IDocumentStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new RuntimeException("Unknown document type: " + type);
        }
        return strategy;
    }
}
