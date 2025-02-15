package com.erp.core.app.controller;

import com.erp.core.app.dto.BillDto;
import com.erp.core.app.dto.BillRequestDto;
import com.erp.core.app.model.Bill;
import com.erp.core.app.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class BillController {
    private static final Logger logger = LoggerFactory.getLogger(BillController.class);

    @Autowired
    BillService billService;

    @PostMapping(value = "/bill/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(@Validated @RequestBody BillRequestDto billRequestDto){
        try {
            Bill bill = billService.create(billRequestDto);
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(value="/bill",produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Bill> getAllBill() {
        try {
            return billService.findAll();
        } catch(Exception e) {

            logger.error(e.getMessage(), e);
            return (List<Bill>) ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }

    }

    @GetMapping(value="/bill/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBillById(@PathVariable(value = "id") Long id) {
        try {
            Optional<Bill> bill = billService.findOne(id);
            if (!bill.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Bill Not Found");
            }
            return ResponseEntity.ok().body(bill.get());

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping(value="/bill/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBill(@PathVariable(value = "id") Long id, @RequestBody BillRequestDto billDto) {
        Bill bill = null;
        try {
            bill = billService.update(billDto, id);
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(bill);
    }

    @DeleteMapping(value="/bill/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBill(@PathVariable(value = "id") Long id) {
        try {

            Optional<Bill> bill = billService.findOne(id);
            if (!bill.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Bill Not Found");
            }
            billService.delete(bill.get());
            return ResponseEntity.ok().body(bill.get().getBillId() + "  Successfully Deleted");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }

    }

}
