package com.erp.core.app.controller;

import com.erp.core.app.dto.UserDto;
import com.erp.core.app.jwt.JwtUtils;
import com.erp.core.app.message.request.SignUpForm;
import com.erp.core.app.message.response.ResponseMessage;
import com.erp.core.app.model.Role;
import com.erp.core.app.model.RoleName;
import com.erp.core.app.model.Shop;
import com.erp.core.app.model.User;
import com.erp.core.app.repository.RoleRepository;
import com.erp.core.app.repository.ShopRepository;
import com.erp.core.app.repository.UserRepository;
import com.erp.core.app.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping(value = "/admin/auth/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Validated @RequestBody SignUpForm signUpRequest){
        if(userRepository.existsByUsername(signUpRequest.getUsername())){
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        com.erp.core.app.model.User user = new User();
        BeanUtils.copyProperties(signUpRequest, user);
        String passWord = signUpRequest.getPassword();
        user.setPassword(passwordEncoder.encode(passWord));
        if(passwordEncoder.matches(passWord, user.getPassword())){
            logger.info("Matched");
        }

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "ROLE_ADMIN":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);

                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });

        if(signUpRequest.getShopId() != null) {
            Shop shop = shopRepository.findById(signUpRequest.getShopId())
                    .orElseThrow(() -> new RuntimeException("Shop not found"));
            user.setShop(shop);
        }

        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(new ResponseMessage("User Created successfully!"), HttpStatus.OK);
    }

    @GetMapping(value = "/admin/users", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userDetailsService.findAll();
    }

    @GetMapping(value = "/admin/userid/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id) {
        Optional<User> user = userDetailsService.findOne(id);
        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping(value="/admin/userid/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long id, @Validated @RequestBody UserDto userDTO) {
        User userUpdate = null;
        try {
            if(userDTO.getPassword() != null) {
                userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            userUpdate = userDetailsService.update(userDTO, id);
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(userUpdate);
    }

    @DeleteMapping(value="/admin/userid/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id) {
        try {

            Optional<User> user = userDetailsService.findOne(id);
            if (!user.isPresent()) {
                return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body("User Not Found");
            }
            userDetailsService.delete(user.get());
            return ResponseEntity.ok(Map.of("message", "User deleted successfully!"));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error deleting user"));

        }
    }
}
