package com.erp.core.app.message.request;

import com.erp.core.app.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.springframework.lang.NonNull;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {

    @NonNull
    private String name;

    @NonNull
    private String username;

    @NaturalId
    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    private String phone;

    @NonNull
    private String location;

    private String address;

    private Long shopId;

    private Set<String> roles;
}
