package com.erp.core.app.service;

import com.erp.core.app.dto.ExpenseItemDto;
import com.erp.core.app.model.ExpenseItem;
import com.erp.core.app.repository.ExpenseItemRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseItemService {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseItemService.class);

    @Autowired
    private ExpenseItemRepository expenseItemRepository;

    @Transactional
    public ExpenseItem create(ExpenseItemDto expenseItemDto) throws ParseException {
        ExpenseItem expenseItem= new ExpenseItem();
        BeanUtils.copyProperties(expenseItemDto, expenseItem);
        return expenseItemRepository.save(expenseItem);
    }

    public List<ExpenseItem> findAll() {
        return expenseItemRepository.findAll();
    }

    public Optional<ExpenseItem> findOne(Long id) {
        return expenseItemRepository.findById(id);
    }

    public ExpenseItem update(ExpenseItemDto expenseItemDto, Long id) throws ChangeSetPersister.NotFoundException {
        Optional<ExpenseItem> catOpt =findOne(id);
        if(catOpt.isPresent()){
            ExpenseItem expenseItem = catOpt.get();
            expenseItem.setExpenseItemName(expenseItemDto.getExpenseItemName());
            return expenseItemRepository.saveAndFlush(expenseItem);
        }else{
            throw new ChangeSetPersister.NotFoundException();
        }

    }

    public void delete(ExpenseItem expenseItem) {
        expenseItemRepository.delete(expenseItem);
    }
}
