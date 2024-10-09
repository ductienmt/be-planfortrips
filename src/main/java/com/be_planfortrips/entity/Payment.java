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
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "payment_name", nullable = false, length = 50)
    String paymentName;

    @Column(name = "fee")
    Double fee;

<<<<<<< HEAD
    @Column(name = "description")
    String  description;
=======
    @Column(name = "status",columnDefinition = "status_booking")
    Status status;
>>>>>>> a2b8a3077b0c703f7ee7dd0c64fe44094e080841
}
