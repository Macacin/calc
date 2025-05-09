package com.example.pharmacy.controller;

import com.example.pharmacy.dto.RegistrationDto;
import com.example.pharmacy.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto("", "", ""));
        return "registration";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("registrationDto") @Valid RegistrationDto dto,
            BindingResult binding,
            Model model
    ) {
        if (!dto.password().equals(dto.confirmPassword())) {
            binding.rejectValue("confirmPassword", null, "Пароли не совпадают");
        }
        if (userService.existsByUsername(dto.username())) {
            binding.rejectValue("username", null, "Логин занят");
        }
        if (binding.hasErrors()) {
            return "registration";
        }
        userService.register(dto);
        return "redirect:/login?registered";
    }
}
