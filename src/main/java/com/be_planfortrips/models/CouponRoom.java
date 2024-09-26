package com.be_planfortrips.models;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CouponRoom {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)Long id;@ManyToOne @JoinColumn(name = "coupon_id") Coupon coupon;@ManyToOne@JoinColumn(name = "room_id")Room room;
}
