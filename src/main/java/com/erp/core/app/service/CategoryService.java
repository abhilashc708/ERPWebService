package com.erp.core.app.service;

import com.erp.core.app.dto.CategoryDto;
import com.erp.core.app.model.Category;
import com.erp.core.app.repository.CategoryRepository;
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
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional
    public Category create(CategoryDto categoryDto) throws ParseException {
        Category category= new Category();
        BeanUtils.copyProperties(categoryDto, category);
        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findOne(Long id) {
        return categoryRepository.findById(id);
    }

    public Category update(CategoryDto categoryDto, Long id) throws ChangeSetPersister.NotFoundException {
        Optional<Category> catOpt =findOne(id);
        if(catOpt.isPresent()){
            Category category = catOpt.get();
            category.setCategory(categoryDto.getCategory());
            return categoryRepository.saveAndFlush(category);
        }else{
            throw new ChangeSetPersister.NotFoundException();
        }

    }

    public void delete(Category category) {
        categoryRepository.delete(category);
    }
}
