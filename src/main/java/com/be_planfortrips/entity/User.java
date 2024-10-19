package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "username", length = 50, nullable = true, unique = true)
    String userName;

    @Column(name = "phonenumber", length = 20, nullable = true)
    String phoneNumber;

    @Column(name = "gender", length = 5)
    String gender;

    @Column(name = "password", length = 100, nullable = true)
    String password;

    @Column(name = "address", length = Integer.MAX_VALUE)
    String address;

    @ColumnDefault("true")
    @Column(name = "is_active")
    boolean isActive = true;

    @Column(name = "birthdate", nullable = true)
    Date birthdate;

    @Column(name = "facebook_id")
    String facebookId;

    @Column(name = "google_id")
    String googleId;

    @Column(name = "fullname", length = 50, nullable = true)
    String fullName;

    @Column(name = "email", length = 100, nullable = true, unique = true)
    String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    Image image;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    Role role;


}
