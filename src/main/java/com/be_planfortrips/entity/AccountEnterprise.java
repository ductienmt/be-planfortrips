package com.be_planfortrips.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "account_enterprises")
public class AccountEnterprise extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long accountEnterpriseId;

    @Column(name = "username", length = 20, unique = true)
    String username;

    @Column(name = "password", length = 200)
    String password;

    @Column(name = "email", length = 200,unique = true)
    String email;

    @Column(name = "enterprise_name", length = Integer.MAX_VALUE)
    String enterpriseName;

    @Column(name = "representative", length = 100)
    String representative;

    @Column(name = "tax_code", length = 20)
    String taxCode;

    @Column(name = "phone_number", length = 15,unique = true)
    String phoneNumber;

    @Column(name = "address", length = Integer.MAX_VALUE)
    String address;

    @Column(name = "status")
    boolean status;

    @OneToOne
    @JoinColumn(name = "image_id")
    Image image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_detail_id")
    @JsonBackReference
    TypeEnterpriseDetail typeEnterpriseDetail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    City city;

    @OneToMany(mappedBy = "accountEnterprise", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    List<Coupon> coupons;
}