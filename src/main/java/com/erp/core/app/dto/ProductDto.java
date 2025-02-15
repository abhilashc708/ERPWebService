package com.erp.core.app.dto;

import lombok.Data;
import org.springframework.lang.NonNull;


@Data
public class ProductDto {

    @NonNull
    private String productName;

    private Integer quantity;

    private String units;

    private String mfdDate;

    private String expDate;

    private Double rate;

    private Double actualPrice;

    private Long categoryId;

    private Long shopId;
}
