package com.erp.core.app.service;

import com.erp.core.app.dto.ShopDto;
import com.erp.core.app.model.Shop;
import com.erp.core.app.repository.ShopRepository;
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
public class ShopService {
    private static final Logger logger = LoggerFactory.getLogger(ShopService.class);

    @Autowired
    ShopRepository shopRepository;

    @Transactional
    public Shop create(ShopDto shopDto) throws ParseException {
        Shop shop= new Shop();
        BeanUtils.copyProperties(shopDto, shop);
        return shopRepository.save(shop);
    }

    public List<Shop> findAll() {
        return shopRepository.findAll();
    }

    public Optional<Shop> findOne(Long id) {
        return shopRepository.findById(id);
    }

    public Shop update(ShopDto shopDto, Long id) throws ChangeSetPersister.NotFoundException {
        Optional<Shop> msgOpt =findOne(id);
        if(msgOpt.isPresent()){
            Shop shop = msgOpt.get();
            shop.setName(shopDto.getName());
            shop.setPhone(shopDto.getPhone());
            shop.setEmail(shopDto.getEmail());
            shop.setAddress(shopDto.getAddress());
            shop.setLocation(shopDto.getLocation());
            return shopRepository.saveAndFlush(shop);
        }else{
            throw new ChangeSetPersister.NotFoundException();
        }

    }

    public void delete(Shop shop) {
        shopRepository.delete(shop);
    }
}
