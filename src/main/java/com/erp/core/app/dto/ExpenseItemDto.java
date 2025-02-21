package com.erp.core.app.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class ExpenseItemDto {
    @NonNull
    private String expenseItemName;
}
