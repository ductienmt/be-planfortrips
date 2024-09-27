package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AccountEnterprise extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "username", length = 20)
    String username;
    @Column(name = "password", length = 200)
    String password;
    @Column(name = "email", length = 200)
    String email;
    @Column(name = "enterprisename", length = Integer.MAX_VALUE)
    String enterpriseName;
    @Column(name = "representative", length = 100)
    String representative;
    @Column(name = "taxcode", length = 20)
    String taxCode;
    @Column(name = "phonenumber", length = 15)
    String phoneNumber;
    @Column(name = "address", length = Integer.MAX_VALUE)
    String address;
    @ColumnDefault("true")
    @Column(name = "status")
    boolean status; // @OneTo // @JoinColumn(nm = "image_id")
    // Image ima
    // @OneToMany
    // List<Hotel> hotels;
    
    @OneToMany
    List<EnterpriseImage> enterpriseImages;
