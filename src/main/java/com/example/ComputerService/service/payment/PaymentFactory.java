package com.example.ComputerService.service.payment;

import com.example.ComputerService.model.enums.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentFactory {
    private final Map<PaymentMethod, IPaymentStrategy> strategies;

    public PaymentFactory(List<IPaymentStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(IPaymentStrategy::getMethod, Function.identity()));
    }

    public IPaymentStrategy getStrategy(PaymentMethod method) {
        return Optional.ofNullable(strategies.get(method))
                .orElseThrow(() -> new RuntimeException("Not supported payment method: " + method));
    }
}
