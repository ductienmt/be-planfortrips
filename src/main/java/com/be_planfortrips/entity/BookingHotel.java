package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "booking_hotels")
public class BookingHotel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    Payment payment;

    @Column(name = "total_price",precision = 10, scale = 2)
    BigDecimal totalPrice;

    @ColumnDefault("Pending")
    @Column(name = "status", columnDefinition = "status_booking")
    Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "bookingHotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<BookingHotelDetail> bookingHotelDetails = new HashSet<>();
}
