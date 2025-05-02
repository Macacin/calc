package com.example.calculator.repository;

import com.example.calculator.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    ExchangeRate findByCurrencyPair(String currencyPair);
}