package com.erp.core.app.controller;

import com.erp.core.app.dto.ExpenseItemDto;
import com.erp.core.app.model.ExpenseItem;
import com.erp.core.app.service.ExpenseItemService;
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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class ExpenseItemController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseItemController.class);

    @Autowired
    private ExpenseItemService expenseItemService;

    @PostMapping(value = "/expense/item/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createExpense(@Validated @RequestBody ExpenseItemDto expenseItemDto){
        try {
            ExpenseItem expenseItem = expenseItemService.create(expenseItemDto);
            return ResponseEntity.ok(expenseItem);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(value="/expense/item",produces=MediaType.APPLICATION_JSON_VALUE)
    public List<ExpenseItem> getAllExpenseItems() {
        try {
            return expenseItemService.findAll();
        } catch(Exception e) {

            logger.error(e.getMessage(), e);
            return (List<ExpenseItem>) ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }

    }

    @GetMapping(value="/expense/item/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getExpenseItemById(@PathVariable(value = "id") Long id) {
        try {
            Optional<ExpenseItem> expenseItem = expenseItemService.findOne(id);
            if (!expenseItem.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Expense Item Not Found");
            }
            return ResponseEntity.ok().body(expenseItem.get());

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping(value="/expense/item/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateExpenseItem(@PathVariable(value = "id") Long id, @RequestBody ExpenseItemDto expenseItemDto) {
        ExpenseItem expenseItem = null;
        try {
            expenseItem = expenseItemService.update(expenseItemDto, id);
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(expenseItem);
    }

    @DeleteMapping(value="/expense/item/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteExpenseItem(@PathVariable(value = "id") Long id) {
        try {

            Optional<ExpenseItem> category = expenseItemService.findOne(id);
            if (!category.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Expense Item Not Found");
            }
            expenseItemService.delete(category.get());
            return ResponseEntity.ok(Map.of("message", "Expense Item deleted successfully!"));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error deleting Expense Item"));

        }

    }
}
