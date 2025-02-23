package com.erp.core.app.service;

import com.erp.core.app.dto.ReportDto;
import com.erp.core.app.model.Bill;
import com.erp.core.app.model.Expense;
import com.erp.core.app.repository.BillRepository;
import com.erp.core.app.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    public ReportDto getReport(Date startDate, Date endDate) {
        Date normalizedStart = getStartOfDay(startDate);
        Date normalizedEnd = getEndOfDay(endDate);
        // Fetch sales and expenses from DB
        List<Bill> sales = billRepository.findByBillingDateBetween(startDate, endDate);
        List<Expense> expenses = expenseRepository.findByCreatedAtBetween(normalizedStart, normalizedEnd);

        // Calculate totals
        double totalSales = sales.stream().mapToDouble(Bill::getTotalAmount).sum();
        double totalExpenses = expenses.stream().mapToDouble(Expense::getRate).sum();
        double totalProfit = totalSales - totalExpenses;

        // Return detailed transactions in the response
        return new ReportDto(totalSales, totalExpenses, totalProfit, sales, expenses);
    }

    private Date getStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date getEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}
