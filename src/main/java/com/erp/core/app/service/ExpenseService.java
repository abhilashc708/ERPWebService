package com.erp.core.app.service;

import com.erp.core.app.dto.ExpenseDto;
import com.erp.core.app.model.*;
import com.erp.core.app.repository.ExpenseItemRepository;
import com.erp.core.app.repository.ExpenseRepository;
import com.erp.core.app.repository.ShopRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpenseService {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseItemRepository expenseItemRepository;

    @Autowired
    private ShopRepository shopRepository;


    @Transactional
    public Expense createExpense(ExpenseDto expenseDto) throws ParseException {

        Optional<ExpenseItem> expenseItemOptional = expenseItemRepository.findById(expenseDto.getExpenseItemId());

        if (!expenseItemOptional.isPresent()) {
            throw new RuntimeException("Expense Item not found with ID: " + expenseDto.getExpenseItemId());
        }
        ExpenseItem expenseItem = expenseItemOptional.get();

        Optional<Shop> shopOptional = shopRepository.findById(expenseDto.getShopId());

        if (!shopOptional.isPresent()) {
            throw new RuntimeException("Shop not found with ID: " + expenseDto.getShopId());
        }

        Shop shop = shopOptional.get();
        Expense expense= new Expense();
        BeanUtils.copyProperties(expenseDto, expense);
        // ✅ Get Logged-in Username
        String loggedInUser = getLoggedInUsername();
        expense.setCreatedBy(loggedInUser); // ✅ Store username
        expense.setExpenseItem(expenseItem);
        expense.setExpenseBillCode(generateCode());
        expense.setShop(shop);
        return expenseRepository.save(expense);
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> findOne(Long id) {
        return expenseRepository.findById(id);
    }

    public Expense updateExpense(ExpenseDto expenseDto, Long id) throws ChangeSetPersister.NotFoundException, ParseException {

        Optional<Expense> prdOpt =findOne(id);
        if(prdOpt.isPresent()){
            Expense expense = prdOpt.get();
            expense.setPaymentMethod(expenseDto.getPaymentMethod());
            expense.setShop(expense.getShop());
            expense.setRate(expenseDto.getRate());
            expense.setExpenseItem(expense.getExpenseItem());
            expense.setCreatedBy(expense.getCreatedBy());
            expense.setExpenseBillCode(expense.getExpenseBillCode());
            return expenseRepository.saveAndFlush(expense);
        }else{
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    public void delete(Expense expense) {
        expenseRepository.delete(expense);
    }

    // ✅ Helper method to get the logged-in username
    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // Get username from authenticated user
        } else {
            return principal.toString();
        }
    }

        public static String generateCode() {
            return "TJ" + UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }
}
