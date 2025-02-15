package com.erp.core.app.service;

import com.erp.core.app.dto.BillDto;
import com.erp.core.app.dto.BillItemRequestDto;
import com.erp.core.app.dto.BillRequestDto;
import com.erp.core.app.model.Bill;
import com.erp.core.app.model.BillItem;
import com.erp.core.app.model.Product;
import com.erp.core.app.model.Shop;
import com.erp.core.app.repository.BillRepository;
import com.erp.core.app.repository.ProductRepository;
import com.erp.core.app.repository.ShopRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BillService {

    private static final Logger logger = LoggerFactory.getLogger(BillService.class);

    @Autowired
    BillRepository billRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShopRepository shopRepository;


    @Transactional
    public Bill create(BillRequestDto billRequestDto) throws ParseException {

        Shop shop = shopRepository.findById(billRequestDto.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        Bill bill = Bill.builder()
                .billCode(billRequestDto.getBillCode())
                .phone(billRequestDto.getPhone())
                .billingDate(billRequestDto.getBillingDate())
                .paymentMethod(billRequestDto.getPaymentMethod())
                .shop(shop)
                .build();

        List<BillItem> billItems = billRequestDto.getItems().stream().map(itemDTO -> {
            Product product = productRepository.findById(itemDTO.getItemId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Deduct stock from the product table
            productRepository.reduceStock(product.getProductId(), itemDTO.getQuantity());


            return BillItem.builder()
                    .bill(bill)
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .price(itemDTO.getPrice())
                    .build();
        }).collect(Collectors.toList());
        bill.setItems(billItems);
        return billRepository.save(bill);
    }

    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    public Optional<Bill> findOne(Long id) {
        return billRepository.findById(id);
    }

    public Bill update(BillRequestDto request, Long id) throws ChangeSetPersister.NotFoundException {
        Optional<Bill> billOpt =findOne(id);
        if(billOpt.isPresent()){
            Bill bill = billOpt.get();
            bill.setPhone(request.getPhone());
            bill.setShop(bill.getShop());
            bill.setBillCode(bill.getBillCode());
            List<BillItem> billItems = new ArrayList<>();
            for (BillItemRequestDto itemRequest : request.getItems()) {
                Product product = productRepository.findById(itemRequest.getItemId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                BillItem billItem = new BillItem();
                billItem.setBill(bill);
                billItem.setProduct(product);
                billItem.setQuantity(itemRequest.getQuantity());
                billItem.setPrice(itemRequest.getPrice());

                billItems.add(billItem);
            }
            bill.setItems(billItems);
            return billRepository.saveAndFlush(bill);
        }else{
            throw new ChangeSetPersister.NotFoundException();
        }

    }

    public void delete(Bill bill) {
        billRepository.delete(bill);
    }
}
