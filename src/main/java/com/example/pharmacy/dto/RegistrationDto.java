package com.example.pharmacy.dto;

import javax.validation.constraints.NotBlank;

public record RegistrationDto(
        @NotBlank(message = "Логин обязателен") String username,
        @NotBlank(message = "Пароль обязателен") String password,
        @NotBlank(message = "Подтвердите пароль") String confirmPassword
) {}
