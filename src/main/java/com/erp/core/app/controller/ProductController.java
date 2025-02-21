package com.erp.core.app.controller;

import com.erp.core.app.dto.ProductDto;
import com.erp.core.app.model.Product;
import com.erp.core.app.service.ProductService;
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
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @PostMapping(value = "/product/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(@Validated @RequestBody ProductDto productDto){
        try {
            Product product = productService.create(productDto);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(value="/product",produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getAllProduct() {
        try {
            return productService.findAll();
        } catch(Exception e) {

            logger.error(e.getMessage(), e);
            return (List<Product>) ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }

    }

    @GetMapping(value="/product/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProductById(@PathVariable(value = "id") Long id) {
        try {
            Optional<Product> product = productService.findOne(id);
            if (!product.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Product Not Found");
            }
            return ResponseEntity.ok().body(product.get());

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping(value="/product/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduct(@PathVariable(value = "id") Long id, @RequestBody ProductDto productDto) {
        Product product = null;
        try {
            product = productService.update(productDto, id);
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping(value="/product/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long id) {
        try {

            Optional<Product> product = productService.findOne(id);
            if (!product.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Product Not Found");
            }
            productService.delete(product.get());
            return ResponseEntity.ok(Map.of("message", "Item deleted successfully!"));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error deleting item"));
        }
    }
}
