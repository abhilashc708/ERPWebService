package com.erp.core.app.repository;

import com.erp.core.app.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    // ✅ Custom Query Method to filter bills by date range
    List<Bill> findByBillingDateBetween(Date startDate, Date endDate);
}
