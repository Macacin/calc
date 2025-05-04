package com.example.pharmacy.controller;

import com.example.pharmacy.dto.DrugDto;
import com.example.pharmacy.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@Controller
@RequestMapping("/drugs-ui")
@RequiredArgsConstructor
@Validated
public class DrugUiController {

    private final DrugService drugService;

    @GetMapping
    public String showDrugs(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {
        Sort.Direction dir = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        var page = drugService.list(filter,
                PageRequest.of(0, Integer.MAX_VALUE, Sort.by(dir, sortField)));

        model.addAttribute("drugs", page.getContent());
        model.addAttribute("filter", filter);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "drugs-ui";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("drug", new DrugDto(
                null, "", null, null, BigDecimal.ZERO, ""
        ));
        return "drug-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        DrugDto dto = drugService.get(id);
        model.addAttribute("drug", dto);
        return "drug-form";
    }

    @PostMapping
    public String save(@ModelAttribute("drug") @Valid DrugDto dto) {
        if (dto.id() == null) drugService.create(dto);
        else drugService.update(dto.id(), dto);
        return "redirect:/drugs-ui";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        drugService.delete(id);
        return "redirect:/drugs-ui";
    }
}