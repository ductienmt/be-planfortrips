package com.be_planfortrips.entity;
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
    @Column(columnDefinition = "varchar(20)",unique = true)
    String username;
    @Column(columnDefinition = "varchar(200)")
    String password;
    @Column(columnDefinition = "varchar(200)",unique = true)
    String email;
    @Column(columnDefinition = "text")
    String enterpriseName;
    @Column(columnDefinition = "varchar(100)")
    String representative;
    @Column(columnDefinition = "varchar(20)",nullable = false)
    String taxCode;
    @Column(columnDefinition = "varchar(20)",unique = true)
    String phoneNumber;
    @Column(columnDefinition = "text")
    String address;
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
