package com.example.pharmacy.repository;

import com.example.pharmacy.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer,Long> {
    Optional<Manufacturer> findByName(String name);
}
