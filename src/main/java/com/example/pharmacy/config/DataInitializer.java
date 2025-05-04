package com.example.pharmacy.config;

import com.example.pharmacy.model.Category;
import com.example.pharmacy.model.Manufacturer;
import com.example.pharmacy.model.Drug;
import com.example.pharmacy.model.User;
import com.example.pharmacy.model.Role;
import com.example.pharmacy.repository.CategoryRepository;
import com.example.pharmacy.repository.ManufacturerRepository;
import com.example.pharmacy.repository.DrugRepository;
import com.example.pharmacy.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initData(
            CategoryRepository categoryRepository,
            ManufacturerRepository manufacturerRepository,
            DrugRepository drugRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // === Сид категорий ===
            Category analgesics;
            Category antibiotics;
            Category vitamins;

            if (categoryRepository.count() == 0) {
                analgesics = categoryRepository.save(new Category(null, "Анальгетики", null));
                antibiotics = categoryRepository.save(new Category(null, "Антибиотики", null));
                vitamins = categoryRepository.save(new Category(null, "Витамины", null));
            } else {
                analgesics = categoryRepository.findByName("Анальгетики").orElseThrow();
                antibiotics = categoryRepository.findByName("Антибиотики").orElseThrow();
                vitamins = categoryRepository.findByName("Витамины").orElseThrow();
            }

            Manufacturer pfizer;
            Manufacturer roche;
            Manufacturer bayer;

            if (manufacturerRepository.count() == 0) {
                pfizer = manufacturerRepository.save(new Manufacturer(null, "Pfizer", "США", null));
                roche = manufacturerRepository.save(new Manufacturer(null, "Roche", "Швейцария", null));
                bayer = manufacturerRepository.save(new Manufacturer(null, "Bayer", "Германия", null));
            } else {
                pfizer = manufacturerRepository.findByName("Pfizer").orElseThrow();
                roche = manufacturerRepository.findByName("Roche").orElseThrow();
                bayer = manufacturerRepository.findByName("Bayer").orElseThrow();
            }

            if (drugRepository.count() == 0) {
                drugRepository.save(new Drug(null, "Ибупрофен", analgesics, pfizer,
                        new BigDecimal("199.99"), "Обезболивающее средство"));
                drugRepository.save(new Drug(null, "Амоксициллин", antibiotics, roche,
                        new BigDecimal("299.99"), "Антибиотик широкого спектра действия"));
                drugRepository.save(new Drug(null, "Витамин C", vitamins, bayer,
                        new BigDecimal("149.50"), "Укрепляет иммунную систему"));
            }

            if (userRepository.count() == 0) {
                // Админ
                userRepository.save(createUser("admin", "admin", Role.ROLE_ADMIN, passwordEncoder));
                System.out.println(">>> Default admin created: login=admin, password=admin");

                userRepository.save(createUser("user1", "user1pass", Role.ROLE_USER, passwordEncoder));
                System.out.println(">>> User created: login=user1, password=user1pass");
                userRepository.save(createUser("user2", "user2pass", Role.ROLE_USER, passwordEncoder));
                System.out.println(">>> User created: login=user2, password=user2pass");
            }
        };
    }

    private User createUser(String username, String rawPassword, Role role, PasswordEncoder encoder) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(rawPassword));
        user.setRole(role);
        return user;
    }
}
