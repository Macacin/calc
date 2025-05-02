package com.example.calculator.config;

import com.example.calculator.model.ExchangeRate;
import com.example.calculator.model.User;
import com.example.calculator.repository.ExchangeRateRepository;
import com.example.calculator.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   ExchangeRateRepository exchangeRateRepository) {
        return args -> {
            if (userRepository.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole("ROLE_ADMIN");
                userRepository.save(admin);
            }

            if (exchangeRateRepository.count() == 0) {
                ExchangeRate usdToEur = new ExchangeRate();
                usdToEur.setCurrencyPair("USD-EUR");
                usdToEur.setRate(0.93);
                exchangeRateRepository.save(usdToEur);

                ExchangeRate eurToUsd = new ExchangeRate();
                eurToUsd.setCurrencyPair("EUR-USD");
                eurToUsd.setRate(1.07);
                exchangeRateRepository.save(eurToUsd);
            }
        };
    }
}