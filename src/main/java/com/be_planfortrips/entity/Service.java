package com.be_planfortrips.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String serviceName;
    Double price;
}
