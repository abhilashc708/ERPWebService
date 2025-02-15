package com.erp.core.app.service;

import com.erp.core.app.dto.UserDto;
import com.erp.core.app.model.Role;
import com.erp.core.app.model.Shop;
import com.erp.core.app.model.User;
import com.erp.core.app.repository.RoleRepository;
import com.erp.core.app.repository.ShopRepository;
import com.erp.core.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ShopRepository shopRepository;


    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList())
        );
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(Long id) {
        return userRepository.findById(id);
    }

    public User update(UserDto userDTO, Long id) throws ChangeSetPersister.NotFoundException {
        Optional<User> usrOpt =findOne(id);
        if(usrOpt.isPresent()){
            User usr = usrOpt.get();
            if(userDTO.getPassword() == null) {
                usr.setPassword(usr.getPassword());
            }
            else
            {
                usr.setPassword(userDTO.getPassword());
            }
            usr.setName(userDTO.getName());
            usr.setUsername(usr.getUsername());
            usr.setPhone(userDTO.getPhone());
            usr.setLocation(userDTO.getLocation());
            usr.setAddress(userDTO.getAddress());

            Shop shop = shopRepository.findById(userDTO.getShopId())
                    .orElseThrow(() -> new RuntimeException("Shop not found"));
            usr.setShop(shop);
            usr.setRoles(usr.getRoles());

            if((userDTO.getEmail()).equals(usr.getEmail()) )
            {
                usr.setEmail(userDTO.getEmail());
            }
            else
            {
                throw new ChangeSetPersister.NotFoundException();
            }

            return userRepository.saveAndFlush(usr);
        }else{
            throw new ChangeSetPersister.NotFoundException();
        }

    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}
