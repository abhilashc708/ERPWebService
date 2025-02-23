package com.erp.core.app.dto;

import lombok.Data;

@Data
public class ExpenseDto {

    private Long expenseItemId;

    private String expenseBillCode;

    private Long shopId;

    private Integer rate;

    private String paymentMethod;

}
