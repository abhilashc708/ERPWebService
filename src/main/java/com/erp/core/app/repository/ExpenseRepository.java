package com.erp.core.app.repository;

import com.erp.core.app.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // ✅ Custom Query Method to filter expense by date range
    List<Expense> findByCreatedAtBetween(Date startDate, Date endDate);
}
