package com.example.pharmacy.repository;

import com.example.pharmacy.model.Drug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface DrugRepository extends JpaRepository<Drug, Long> {
    Page<Drug> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByCategoryId(Long categoryId);

    @Query("SELECT AVG(d.price) FROM Drug d")
    BigDecimal findAveragePrice();
}
