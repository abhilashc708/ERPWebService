package com.erp.core.app.service;

import com.erp.core.app.dto.ProductDto;
import com.erp.core.app.model.Category;
import com.erp.core.app.model.Product;
import com.erp.core.app.model.Shop;
import com.erp.core.app.repository.CategoryRepository;
import com.erp.core.app.repository.ProductRepository;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ShopRepository shopRepository;

    @Transactional
    public Product create(ProductDto productDto) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Optional<Category> categoryOptional = categoryRepository.findById(productDto.getCategoryId());

        if (!categoryOptional.isPresent()) {
            throw new RuntimeException("Category not found with ID: " + productDto.getCategoryId());
        }

        Category category = categoryOptional.get();

        Optional<Shop> shopOptional = shopRepository.findById(productDto.getShopId());

        if (!shopOptional.isPresent()) {
            throw new RuntimeException("Shop not found with ID: " + productDto.getShopId());
        }

        Shop shop = shopOptional.get();
        Product product= new Product();
        BeanUtils.copyProperties(productDto, product);
        // ✅ Convert `DD-MM-YYYY` String to `Date`
        product.setMfdDate(sdf.parse(productDto.getMfdDate()));
        product.setExpDate(productDto.getExpDate() != null ? sdf.parse(productDto.getExpDate()) : null);
        // ✅ Get Logged-in Username
        String loggedInUser = getLoggedInUsername();
        product.setCreatedBy(loggedInUser); // ✅ Store username
        product.setCategory(category);
        product.setShop(shop);
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findOne(Long id) {
        return productRepository.findById(id);
    }

    public Product update(ProductDto productDto, Long id) throws ChangeSetPersister.NotFoundException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Optional<Product> prdOpt =findOne(id);
        if(prdOpt.isPresent()){
            Product product = prdOpt.get();
           product.setProductName(productDto.getProductName());
           product.setRate(productDto.getRate());
            product.setMfdDate(sdf.parse(productDto.getMfdDate()));
            product.setExpDate(productDto.getExpDate() != null ? sdf.parse(productDto.getExpDate()) : null);
//           product.setExpDate(productDto.getExpDate());
//           product.setMfdDate(productDto.getMfdDate());
           product.setShop(product.getShop());
           product.setQuantity(productDto.getQuantity());
           product.setCreatedBy(product.getCreatedBy());
           product.setActualPrice(product.getActualPrice());
           product.setCategory(product.getCategory());
           product.setUnits(product.getUnits());
            return productRepository.saveAndFlush(product);
        }else{
            throw new ChangeSetPersister.NotFoundException();
        }

    }

    public void delete(Product product) {
        productRepository.delete(product);
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

}
