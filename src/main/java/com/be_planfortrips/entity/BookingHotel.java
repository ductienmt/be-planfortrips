package com.be_planfortrips.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class BookingHotel extends BaseEntity{
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;
    Date checkinTime;
    Date checkoutTime;
    Double totalPrice;
    Status status;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    Payment payment;
}
