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

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "payment_method", nullable = false, length = 50)
    String paymentMethod;

    @Column(name = "amount", precision = 10, scale = 2)
    BigDecimal amount;

    @Column(name = "status")
    Status status;
}
