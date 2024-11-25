package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "admins")
public class Admin{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "username", length = 20)
    String userName;

    @Column(name = "password", length = 100)
    String password;

    @Column(name = "full_name", length = 50)
    String fullName;

    @Column(name = "email", length = 200)
    String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonIgnore
    private Role role;



}
