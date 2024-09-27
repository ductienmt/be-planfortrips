package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(columnDefinition = "varchar(20)")
    String userName;
    @Column(columnDefinition = "varchar(200)")
    String password;
    @Column(columnDefinition = "varchar(50)")
    String fullName;
    @Column(columnDefinition = "varchar(200)")
    String email;

}
