package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

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

    @Column(name = "username", length = 50)
    String userName;

    @Column(name = "phonenumber", length = 20)
    String phoneNumber;

    @Column(name = "gender", length = 5)
    String gender;

    @Column(name = "password", length = 100)
    String password;

    @Column(name = "address", length = Integer.MAX_VALUE)
    String address;

    @Column(name = "is_active")
    boolean isActive;

    @Column(name = "birthdate")
    Date birthdate;

    @ColumnDefault("0")
    @Column(name = "facebook_account_id")
    int isFacebook;

    @ColumnDefault("0")
    @Column(name = "google_account_id")
    int isGoogle;

    @Column(name = "fullname", length = 50)
    String fullName;

    @Column(name = "email", length = 100)
    String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    Image image;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    SocialAccount socialAccount;
}
