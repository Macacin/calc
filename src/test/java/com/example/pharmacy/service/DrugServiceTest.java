package com.example.pharmacy.service;

import com.example.pharmacy.dto.DrugDto;
import com.example.pharmacy.mapper.DrugMapper;
import com.example.pharmacy.model.Category;
import com.example.pharmacy.model.Drug;
import com.example.pharmacy.model.Manufacturer;
import com.example.pharmacy.repository.CategoryRepository;
import com.example.pharmacy.repository.DrugRepository;
import com.example.pharmacy.repository.ManufacturerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrugServiceTest {

    @Mock
    private DrugRepository drugRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private DrugMapper drugMapper;

    @InjectMocks
    private DrugService drugService;

    private Drug sampleDrug;
    private DrugDto sampleDto;
    private Category sampleCategory;
    private Manufacturer sampleManufacturer;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category();
        sampleCategory.setId(1L);
        sampleCategory.setName("TestCat");

        sampleManufacturer = new Manufacturer();
        sampleManufacturer.setId(2L);
        sampleManufacturer.setName("TestMan");
        sampleManufacturer.setCountry("TestLand");

        sampleDrug = new Drug();
        sampleDrug.setId(10L);
        sampleDrug.setName("TestDrug");
        sampleDrug.setCategory(sampleCategory);
        sampleDrug.setManufacturer(sampleManufacturer);
        sampleDrug.setPrice(new BigDecimal("123.45"));
        sampleDrug.setDescription("Desc");

        sampleDto = DrugDto.builder()
                .id(10L)
                .name("TestDrug")
                .categoryId(1L)
                .manufacturerId(2L)
                .price(new BigDecimal("123.45"))
                .description("Desc")
                .build();
    }

    @Test
    void testGet_existing() {
        when(drugRepository.findById(10L)).thenReturn(Optional.of(sampleDrug));
        when(drugMapper.toDto(sampleDrug)).thenReturn(sampleDto);

        DrugDto result = drugService.get(10L);

        assertEquals(sampleDto, result);
        verify(drugRepository).findById(10L);
        verify(drugMapper).toDto(sampleDrug);
    }

    @Test
    void testGet_notFound() {
        when(drugRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> drugService.get(5L));
        verify(drugRepository).findById(5L);
    }

    @Test
    void testList_noFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Drug> page = new PageImpl<>(List.of(sampleDrug));
        when(drugRepository.findAll(pageable)).thenReturn(page);
        when(drugMapper.toDto(sampleDrug)).thenReturn(sampleDto);

        Page<DrugDto> result = drugService.list(null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(sampleDto, result.getContent().get(0));
        verify(drugRepository).findAll(pageable);
        verify(drugMapper).toDto(sampleDrug);
    }

    @Test
    void testCreate_success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        when(manufacturerRepository.findById(2L)).thenReturn(Optional.of(sampleManufacturer));
        when(drugRepository.save(any(Drug.class))).thenReturn(sampleDrug);
        when(drugMapper.toDto(sampleDrug)).thenReturn(sampleDto);

        DrugDto result = drugService.create(sampleDto);

        assertEquals(sampleDto, result);
        verify(categoryRepository).findById(1L);
        verify(manufacturerRepository).findById(2L);
        verify(drugRepository).save(any(Drug.class));
        verify(drugMapper).toDto(sampleDrug);
    }

    @Test
    void testCreate_categoryNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> drugService.create(sampleDto));
        verify(categoryRepository).findById(1L);
    }

    @Test
    void testDelete_success() {
        when(drugRepository.existsById(10L)).thenReturn(true);

        assertDoesNotThrow(() -> drugService.delete(10L));
        verify(drugRepository).existsById(10L);
        verify(drugRepository).deleteById(10L);
    }

    @Test
    void testDelete_notFound() {
        when(drugRepository.existsById(5L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> drugService.delete(5L));
        verify(drugRepository).existsById(5L);
    }

    @Test
    void testCountByCategory() {
        when(drugRepository.countByCategoryId(1L)).thenReturn(3L);

        long count = drugService.countByCategory(1L);

        assertEquals(3L, count);
        verify(drugRepository).countByCategoryId(1L);
    }

    @Test
    void testAvgPrice() {
        BigDecimal avg = new BigDecimal("50.00");
        when(drugRepository.findAveragePrice()).thenReturn(avg);

        BigDecimal result = drugService.avgPrice();

        assertEquals(avg, result);
        verify(drugRepository).findAveragePrice();
    }
}
