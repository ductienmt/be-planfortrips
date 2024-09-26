package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="users")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String userName;
    String phoneNumber;
    String gender;
    String password;
    String address;
    boolean isActive;
    Date dateOfBirth;
    int isFacebook;
    int isGoogle;
    String fullName;
    @ManyToOne
    @JoinColumn(name = "image_id")
    Image image;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    SocialAccount socialAccount;
}
