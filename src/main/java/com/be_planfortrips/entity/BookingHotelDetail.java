package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "booking_hotel_details")
public class BookingHotelDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
            @Column(name = "book_hotel_detail_id")
    Integer id;
    @ManyToOne
    @JoinColumn(name = "booking_hotel_id")
    BookingHotel bookingHotel;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    Double totalPrice;
}
