package com.be_planfortrips.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "account_enterprises")
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

    @Column(name = "enterprisename", length = Integer.MAX_VALUE)
    String enterpriseName;

    @Column(name = "representative", length = 100)
    String representative;

    @Column(name = "taxcode", length = 20)
    String taxCode;

    @Column(name = "phonenumber", length = 15,unique = true)
    String phoneNumber;

    @Column(name = "address", length = Integer.MAX_VALUE)
    String address;

    @ColumnDefault("true")
    @Column(name = "status")
    boolean status;

    @OneToOne
    @JoinColumn(name = "image_id")
    Image image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_detail_id")
    @JsonBackReference
    TypeEnterpriseDetail typeEnterpriseDetail;
//    @OneToMany
//    List<Hotel> hotels;
}
