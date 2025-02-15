package com.erp.core.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class BillItemRequestDto {
    private Long categoryId;
    private Long itemId;
    private Integer quantity;
    private Double price;
}
