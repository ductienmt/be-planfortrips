package com.be_planfortrips.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "booking_hotels_details")
public class BookingHotelDetail extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID bookingHotelDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    Room room;

    @Column(name = "check_in_time")
    LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    LocalDateTime checkOutTime;

    @Column(name = "price", precision = 10, scale = 2)
    BigDecimal price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status = Status.Pending;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    BookingHotel bookingHotel;

}
