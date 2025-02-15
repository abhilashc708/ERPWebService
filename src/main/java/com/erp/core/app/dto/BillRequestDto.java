package com.erp.core.app.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class BillRequestDto {
    private String billCode;
    private String phone;
    private Date billingDate;
    private String paymentMethod;
    private Long shopId;
    private List<BillItemRequestDto> items;
}
