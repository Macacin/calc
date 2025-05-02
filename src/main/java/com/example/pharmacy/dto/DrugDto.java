package com.example.pharmacy.dto;

import lombok.Builder;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
public record DrugDto(
        Long id,
        @NotBlank(message = "Название препарата не может быть пустым")
        String name,
        @NotNull(message = "ID категории обязателен")
        Long categoryId,
        @NotNull(message = "ID производителя обязателен")
        Long manufacturerId,
        @NotNull(message = "Цена не может быть пустой")
        @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть положительной")
        BigDecimal price,
        String description
) {}
