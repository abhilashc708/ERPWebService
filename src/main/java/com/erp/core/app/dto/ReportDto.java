package com.erp.core.app.dto;

import com.erp.core.app.model.Bill;
import com.erp.core.app.model.Expense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private double totalSales;
    private double totalExpenses;
    private double totalProfit;
    private List<Bill> salesTransactions;
    private List<Expense> expenseTransactions;

}
