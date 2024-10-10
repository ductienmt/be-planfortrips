package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "total_price", precision = 10, scale = 2)
    BigDecimal totalPrice;

    @Column(name = "discount_price", precision = 10, scale = 2)
    BigDecimal discountPrice;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "booking_time")
    Instant bookingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    Payment payment;

    @Column(name = "status", columnDefinition = "status_booking")
//    @Enumerated(EnumType.STRING)
    Status status;

}