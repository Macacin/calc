package com.example.calculator.service;

import com.example.calculator.model.ExchangeRate;
import com.example.calculator.model.OperationHistory;
import com.example.calculator.repository.ExchangeRateRepository;
import com.example.calculator.repository.OperationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CalculatorService {

    @Autowired private OperationHistoryRepository historyRepository;
    @Autowired private ExchangeRateRepository exchangeRateRepository;

    public List<OperationHistory> getUserHistory(String username, boolean isAdmin) {
        if (isAdmin) {
            return historyRepository.findAllWithUsernameOrderByTimestampDesc();
        } else {
            return historyRepository.findByUsernameOrderByTimestampDesc(username);
        }
    }

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    public void updateExchangeRate(String currencyPair, double rate) {
        ExchangeRate exchangeRate = exchangeRateRepository.findByCurrencyPair(currencyPair);
        if (exchangeRate == null) {
            exchangeRate = new ExchangeRate();
            exchangeRate.setCurrencyPair(currencyPair);
        }
        exchangeRate.setRate(rate);
        exchangeRateRepository.save(exchangeRate);
    }

    public void convertAndSave(double amount, String fromCurrency, String toCurrency, String username) {
        ExchangeRate rate = exchangeRateRepository.findByCurrencyPair(fromCurrency + "-" + toCurrency);
        if (rate != null) {
            double result = amount * rate.getRate();
            saveHistory(username, "CONVERSION",
                    String.format("%.2f %s → %.2f %s", amount, fromCurrency, result, toCurrency));
        }
    }

    public void calculateAndSaveOhmLaw(Double voltage, Double current, Double resistance, String username) {
        double result;
        String details;

        if (voltage == null) {
            result = current * resistance;
            details = String.format("I=%.2f, R=%.2f → V=%.2f", current, resistance, result);
        } else if (current == null) {
            result = voltage / resistance;
            details = String.format("V=%.2f, R=%.2f → I=%.2f", voltage, resistance, result);
        } else {
            result = voltage / current;
            details = String.format("V=%.2f, I=%.2f → R=%.2f", voltage, current, result);
        }

        saveHistory(username, "OHM_LAW", details);
    }

    private void saveHistory(String username, String operationType, String details) {
        OperationHistory history = new OperationHistory();
        history.setUsername(username);
        history.setOperationType(operationType);
        history.setDetails(details);
        history.setTimestamp(LocalDateTime.now());
        historyRepository.save(history);
    }
}