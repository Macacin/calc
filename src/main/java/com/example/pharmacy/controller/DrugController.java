package com.example.pharmacy.controller;

import com.example.pharmacy.dto.DrugDto;
import com.example.pharmacy.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/drugs")
@Validated
@RequiredArgsConstructor
public class DrugController {

    private final DrugService drugService;

    @GetMapping
    public Page<DrugDto> list(
            @RequestParam(required = false) String filter,
            @PageableDefault(sort = "name") Pageable pageable
    ) {
        return drugService.list(filter, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrugDto> getById(@PathVariable Long id) {
        DrugDto dto = drugService.get(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DrugDto create(@Valid @RequestBody DrugDto dto) {
        return drugService.create(dto);
    }

    @PutMapping("/{id}")
    public DrugDto update(
            @PathVariable Long id,
            @Valid @RequestBody DrugDto dto
    ) {
        return drugService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        drugService.delete(id);
    }

    @GetMapping("/stats/avg-price")
    public BigDecimal getAveragePrice() {
        return drugService.avgPrice();
    }

    @GetMapping("/stats/count-by-category/{categoryId}")
    public long getCountByCategory(@PathVariable Long categoryId) {
        return drugService.countByCategory(categoryId);
    }
}
