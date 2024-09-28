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

public class AccountEnterprise extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

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

    @ColumnDefault("true")
    @Column(name = "status")
    boolean status;

    @OneToOne
    @JoinColumn(name = "image_id")
    Image image;

    @ManyToOne
    @JoinColumn(name = "type_detail_id")
    TypeEnterpriseDetail typeEnterpriseDetail;
//    @OneToMany
//    List<Hotel> hotels;
}
