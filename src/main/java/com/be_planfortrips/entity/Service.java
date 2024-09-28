package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    Long id;

    @Column(name = "service_name", length = 50)
    String serviceName;

    @Column(name = "price", precision = 10, scale = 2)
    BigDecimal price;
}
