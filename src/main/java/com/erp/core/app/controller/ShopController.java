package com.erp.core.app.controller;

import com.erp.core.app.dto.ShopDto;
import com.erp.core.app.model.Shop;
import com.erp.core.app.service.ShopService;
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
@RequestMapping("/api/admin")
public class ShopController {
    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    ShopService shopService;

    @PostMapping(value = "/shop/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createShop(@Validated @RequestBody ShopDto shopDto){
        try {
            Shop shop = shopService.create(shopDto);
            return ResponseEntity.ok(shop);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(value="/shop",produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Shop> getAllShop() {
        try {
            return shopService.findAll();
        } catch(Exception e) {

            logger.error(e.getMessage(), e);
            return (List<Shop>) ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }

    }

    @GetMapping(value="/shop/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getShopById(@PathVariable(value = "id") Long id) {
        try {
            Optional<Shop> shop = shopService.findOne(id);
            if (!shop.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Shop Not Found");
            }
            return ResponseEntity.ok().body(shop.get());

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping(value="/shop/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateServiceComment(@PathVariable(value = "id") Long id, @RequestBody ShopDto shopDto) {
        Shop shop = null;
        try {
            shop = shopService.update(shopDto, id);
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(shop);
    }

    @DeleteMapping(value="/shop/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteShop(@PathVariable(value = "id") Long id) {
        try {

            Optional<Shop> shop = shopService.findOne(id);
            if (!shop.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("Shop Not Found");
            }
            shopService.delete(shop.get());
            //return ResponseEntity.ok().body(shop.get().getShopId() + "  Successfully Deleted");
            return ResponseEntity.ok(Map.of("message", "Shop deleted successfully!"));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error deleting shop"));

        }

    }

}
