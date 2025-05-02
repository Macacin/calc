package com.example.pharmacy.mapper;

import com.example.pharmacy.model.Drug;
import com.example.pharmacy.dto.DrugDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DrugMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "manufacturer.id", target = "manufacturerId")
    DrugDto toDto(Drug drug);
}
