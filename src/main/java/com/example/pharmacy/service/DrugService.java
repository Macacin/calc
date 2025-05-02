package com.example.pharmacy.service;

import com.example.pharmacy.dto.DrugDto;
import com.example.pharmacy.mapper.DrugMapper;
import com.example.pharmacy.model.Category;
import com.example.pharmacy.model.Drug;
import com.example.pharmacy.model.Manufacturer;
import com.example.pharmacy.repository.DrugRepository;
import com.example.pharmacy.repository.CategoryRepository;
import com.example.pharmacy.repository.ManufacturerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DrugService {
    private final DrugRepository drugRepository;
    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final DrugMapper drugMapper;

    public Page<DrugDto> list(String filter, Pageable pageable) {
        Page<Drug> page;
        if (filter == null || filter.isBlank()) {
            page = drugRepository.findAll(pageable);
        } else {
            page = drugRepository.findByNameContainingIgnoreCase(filter, pageable);
        }
        return page.map(drugMapper::toDto);
    }

    public DrugDto get(Long id) {
        Drug drug = drugRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Препарат не найден, ID: " + id));
        return drugMapper.toDto(drug);
    }

    public DrugDto create(DrugDto dto) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена, ID: " + dto.categoryId()));
        Manufacturer manufacturer = manufacturerRepository.findById(dto.manufacturerId())
                .orElseThrow(() -> new EntityNotFoundException("Производитель не найден, ID: " + dto.manufacturerId()));
        Drug drug = new Drug();
        drug.setName(dto.name());
        drug.setCategory(category);
        drug.setManufacturer(manufacturer);
        drug.setPrice(dto.price());
        drug.setDescription(dto.description());
        Drug saved = drugRepository.save(drug);
        return drugMapper.toDto(saved);
    }

    public DrugDto update(Long id, DrugDto dto) {
        Drug existing = drugRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Препарат не найден, ID: " + id));
        if (dto.name() != null) {
            existing.setName(dto.name());
        }
        if (dto.categoryId() != null) {
            Category category = categoryRepository.findById(dto.categoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Категория не найдена, ID: " + dto.categoryId()));
            existing.setCategory(category);
        }
        if (dto.manufacturerId() != null) {
            Manufacturer manufacturer = manufacturerRepository.findById(dto.manufacturerId())
                    .orElseThrow(() -> new EntityNotFoundException("Производитель не найден, ID: " + dto.manufacturerId()));
            existing.setManufacturer(manufacturer);
        }
        if (dto.price() != null) {
            existing.setPrice(dto.price());
        }
        existing.setDescription(dto.description());
        Drug updated = drugRepository.save(existing);
        return drugMapper.toDto(updated);
    }

    public void delete(Long id) {
        if (!drugRepository.existsById(id)) {
            throw new EntityNotFoundException("Препарат не найден, ID: " + id);
        }
        drugRepository.deleteById(id);
    }

    public long countByCategory(Long categoryId) {
        return drugRepository.countByCategoryId(categoryId);
    }

    public BigDecimal avgPrice() {
        return drugRepository.findAveragePrice();
    }
}


