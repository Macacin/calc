package com.example.pharmacy.controller;

import com.example.pharmacy.dto.ManufacturerDto;
import com.example.pharmacy.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerRepository manufacturerRepository;

    @GetMapping
    public List<ManufacturerDto> list() {
        return manufacturerRepository.findAll()
                .stream()
                .map(m -> new ManufacturerDto(m.getId(), m.getName(), m.getCountry()))
                .collect(Collectors.toList());
    }
}
