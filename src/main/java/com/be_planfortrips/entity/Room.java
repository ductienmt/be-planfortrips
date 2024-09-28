package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "room_name", length = 100)
    String roomName;

    @Column(name = "price", precision = 10, scale = 2)
    BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    Hotel hotel;

    @ColumnDefault("Standard")
    @Column(name = "type_of_room")
    TypeOfRoom typeOfRoom;

    @Column(name = "description", length = Integer.MAX_VALUE)
    String description;

    @Column(name = "max_size", nullable = false)
    int maxSize;

    @Column(name = "rating")
    int rating;

    @Column(name = "is_available")
    boolean isAvailable;

    @OneToMany
    List<RoomImage> images;

    @OneToMany
    List<CouponRoom> couponRooms;
}
