package com.example.pharmacy.config;

import com.example.pharmacy.model.Category;
import com.example.pharmacy.model.Drug;
import com.example.pharmacy.model.Manufacturer;
import com.example.pharmacy.repository.CategoryRepository;
import com.example.pharmacy.repository.DrugRepository;
import com.example.pharmacy.repository.ManufacturerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(
            CategoryRepository categoryRepository,
            ManufacturerRepository manufacturerRepository,
            DrugRepository drugRepository
    ) {
        return args -> {
            Category analgesics = null;
            Category antibiotics = null;
            Category vitamins = null;

            if (categoryRepository.count() == 0) {
                analgesics = new Category();
                analgesics.setName("Анальгетики");
                analgesics = categoryRepository.save(analgesics);

                antibiotics = new Category();
                antibiotics.setName("Антибиотики");
                antibiotics = categoryRepository.save(antibiotics);

                vitamins = new Category();
                vitamins.setName("Витамины");
                vitamins = categoryRepository.save(vitamins);
            } else {
                for (Category c : categoryRepository.findAll()) {
                    switch (c.getName()) {
                        case "Анальгетики" -> analgesics = c;
                        case "Антибиотики" -> antibiotics = c;
                        case "Витамины" -> vitamins = c;
                    }
                }
            }

            Manufacturer pfizer = null;
            Manufacturer roche = null;
            Manufacturer bayer = null;

            if (manufacturerRepository.count() == 0) {
                pfizer = new Manufacturer();
                pfizer.setName("Pfizer");
                pfizer.setCountry("США");
                pfizer = manufacturerRepository.save(pfizer);

                roche = new Manufacturer();
                roche.setName("Roche");
                roche.setCountry("Швейцария");
                roche = manufacturerRepository.save(roche);

                bayer = new Manufacturer();
                bayer.setName("Bayer");
                bayer.setCountry("Германия");
                bayer = manufacturerRepository.save(bayer);
            } else {
                for (Manufacturer m : manufacturerRepository.findAll()) {
                    switch (m.getName()) {
                        case "Pfizer" -> pfizer = m;
                        case "Roche" -> roche = m;
                        case "Bayer" -> bayer = m;
                    }
                }
            }

            if (drugRepository.count() == 0) {
                Drug ibuprofen = new Drug();
                ibuprofen.setName("Ибупрофен");
                ibuprofen.setCategory(analgesics);
                ibuprofen.setManufacturer(pfizer);
                ibuprofen.setPrice(new BigDecimal("199.99"));
                ibuprofen.setDescription("Обезболивающее средство");
                drugRepository.save(ibuprofen);

                Drug amoxicillin = new Drug();
                amoxicillin.setName("Амоксициллин");
                amoxicillin.setCategory(antibiotics);
                amoxicillin.setManufacturer(roche);
                amoxicillin.setPrice(new BigDecimal("299.99"));
                amoxicillin.setDescription("Антибиотик широкого спектра действия");
                drugRepository.save(amoxicillin);

                Drug vitaminC = new Drug();
                vitaminC.setName("Витамин C");
                vitaminC.setCategory(vitamins);
                vitaminC.setManufacturer(bayer);
                vitaminC.setPrice(new BigDecimal("149.50"));
                vitaminC.setDescription("Укрепляет иммунную систему");
                drugRepository.save(vitaminC);
            }
        };
    }
}
