package com.erp.core.app.controller;

import com.erp.core.app.dto.ExpenseDto;
import com.erp.core.app.model.Expense;
import com.erp.core.app.service.ExpenseService;
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
public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;

    @PostMapping(value = "/expense/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createExpense(@Validated @RequestBody ExpenseDto expenseDto){
        try {
            Expense expense = expenseService.createExpense(expenseDto);
            return ResponseEntity.ok(expense);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(value="/expense",produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Expense> getAllExpenses() {
        try {
            return expenseService.findAll();
        } catch(Exception e) {

            logger.error(e.getMessage(), e);
            return (List<Expense>) ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }

    }

    @GetMapping(value="/expense/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getExpenseById(@PathVariable(value = "id") Long id) {
        try {
            Optional<Expense> expense = expenseService.findOne(id);
            if (!expense.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Expense Not Found");
            }
            return ResponseEntity.ok().body(expense.get());

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping(value="/expense/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateExpense(@PathVariable(value = "id") Long id, @RequestBody ExpenseDto expenseDto) {
        Expense expense = null;
        try {
            expense = expenseService.updateExpense(expenseDto, id);
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().body(expense);
    }

    @DeleteMapping(value="/expense/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteExpense(@PathVariable(value = "id") Long id) {
        try {

            Optional<Expense> product = expenseService.findOne(id);
            if (!product.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Expense Not Found");
            }
            expenseService.delete(product.get());
            return ResponseEntity.ok(Map.of("message", "Expense Item deleted successfully!"));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error deleting Expense Item"));
        }
    }

}
