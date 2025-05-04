package com.example.pharmacy;

import com.example.pharmacy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SeedingTest {
    @Autowired UserRepository userRepo;

    @Test
    void adminIsSeeded() {
        assertTrue(userRepo.findByUsername("admin").isPresent(),
                "Admin должен быть в базе");
    }
}
