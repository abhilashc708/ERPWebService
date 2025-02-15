package com.erp.core.app.dto;

import com.erp.core.app.model.BillItem;
import com.erp.core.app.model.Product;
import com.erp.core.app.model.Shop;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BillDto {

    private String billCode;
    private Date billingDate;

    private String phone;

    private String paymentMethod;

    private Shop shop;

    private Long shopId;

    private List<BillItem> items;
}
