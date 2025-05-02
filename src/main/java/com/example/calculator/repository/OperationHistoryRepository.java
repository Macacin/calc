package com.example.calculator.repository;

import com.example.calculator.model.OperationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OperationHistoryRepository extends JpaRepository<OperationHistory, Long> {
    List<OperationHistory> findByUsernameOrderByTimestampDesc(String username);

    @Query("SELECT h FROM OperationHistory h ORDER BY h.timestamp DESC")
    List<OperationHistory> findAllWithUsernameOrderByTimestampDesc();
}