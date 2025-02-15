package com.erp.core.app.controller;

import com.erp.core.app.dto.CategoryDto;
import com.erp.core.app.model.Category;
import com.erp.core.app.service.CategoryService;
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
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    @PostMapping(value = "/category/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCategory(@Validated @RequestBody CategoryDto categoryDto){
        try {
            Category category = categoryService.create(categoryDto);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(value="/category",produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getAllCategory() {
        try {
            return categoryService.findAll();
        } catch(Exception e) {

            logger.error(e.getMessage(), e);
            return (List<Category>) ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }

    }

    @GetMapping(value="/category/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCategoryById(@PathVariable(value = "id") Long id) {
        try {
            Optional<Category> category = categoryService.findOne(id);
            if (!category.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Category Not Found");
            }
            return ResponseEntity.ok().body(category.get());

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping(value="/category/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCategory(@PathVariable(value = "id") Long id, @RequestBody CategoryDto categoryDto) {
        Category category = null;
        try {
            category = categoryService.update(categoryDto, id);
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(category);
    }

    @DeleteMapping(value="/category/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCategory(@PathVariable(value = "id") Long id) {
        try {

            Optional<Category> category = categoryService.findOne(id);
            if (!category.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Category Not Found");
            }
            categoryService.delete(category.get());
            return ResponseEntity.ok(Map.of("message", "Category deleted successfully!"));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error deleting category"));

        }

    }
}
