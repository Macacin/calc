package com.example.calculator.controller;

import com.example.calculator.model.User;
import com.example.calculator.service.CalculatorService;
import com.example.calculator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    @Autowired private CalculatorService calculatorService;
    @Autowired private UserService userService;

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("history", calculatorService.getUserHistory(username, isAdmin));
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) model.addAttribute("error", true);
        if (logout != null) model.addAttribute("logout", true);
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user, "USER");
        return "redirect:/login";
    }

    @GetMapping("/currency")
    public String currencyConverter(Model model) {
        model.addAttribute("rates", calculatorService.getAllExchangeRates());
        return "currency";
    }

    @PostMapping("/convert")
    public String convertCurrency(@RequestParam double amount,
                                  @RequestParam String fromCurrency,
                                  @RequestParam String toCurrency,
                                  Authentication auth) {
        calculatorService.convertAndSave(amount, fromCurrency, toCurrency, auth.getName());
        return "redirect:/";
    }

    @GetMapping("/ohm-law")
    public String ohmLawCalculator() {
        return "ohm-law";
    }

    @PostMapping("/calculate-ohm")
    public String calculateOhmLaw(@RequestParam(required = false) Double voltage,
                                  @RequestParam(required = false) Double current,
                                  @RequestParam(required = false) Double resistance,
                                  Authentication auth) {
        calculatorService.calculateAndSaveOhmLaw(voltage, current, resistance, auth.getName());
        return "redirect:/";
    }

    @GetMapping("/admin/users")
    public String userManagement(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/edit-user";
    }

    @PostMapping("/admin/users/update")
    public String updateUser(@ModelAttribute User user, @RequestParam(required = false) String newPassword) {
        userService.updateUser(user, newPassword);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/rates")
    public String exchangeRates(Model model) {
        model.addAttribute("rates", calculatorService.getAllExchangeRates());
        return "admin/rates";
    }

    @PostMapping("/admin/rates/update")
    public String updateRate(@RequestParam String currencyPair,
                             @RequestParam double rate) {
        calculatorService.updateExchangeRate(currencyPair, rate);
        return "redirect:/admin/rates";
    }

}