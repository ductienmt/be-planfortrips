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
@Table(name = "type_enterprises")
public class TypeEnterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "nametype", length = 50)
    String nameType;

    @Column(name = "description", length = Integer.MAX_VALUE)
    String description;


}
