package com.erp.core.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;


@Entity
@Table(name = "shop")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    @NonNull
    private String name;

    @NonNull
    private String phone;

    @NonNull
    private String location;

    private String address;

    private String email;


//    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore  // ✅ Prevents infinite recursion
//    private List<Bill> bills;
//
//    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonIgnore
//    private List<User> users = new ArrayList<>();

}
