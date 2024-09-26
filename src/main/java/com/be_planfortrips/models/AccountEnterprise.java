package com.be_planfortrips.models;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AccountEnterprise extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    String password;
    String email;
    String enterpriseName;
    String representative;
    String taxCode;
    String phoneNumber;
    String address;
    boolean status;
    @OneToMany
    List<EnterpriseImage> enterpriseImages;
//    @OneToMany
//    List<Hotel> hotels;
}