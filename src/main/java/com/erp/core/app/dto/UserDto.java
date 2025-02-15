package com.erp.core.app.dto;

import com.erp.core.app.model.Role;
import com.erp.core.app.model.Shop;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {

    private  Long id;

    private String name;

    private String username;

    private String email;

    private String password;

    private String phone;

    private String location;

    private String address;

    private Long shopId;

    private Set<Role> roles = new HashSet<>();

}
