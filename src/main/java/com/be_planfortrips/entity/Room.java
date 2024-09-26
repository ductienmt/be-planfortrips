package com.be_planfortrips.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Room {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String roomName;
    Double price;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    Hotel hotel;
    TypeOfRoom typeOfRoom;
    String description;
    int maxSize;
    int rating;
    boolean isAvailable;
    @OneToMany
    List<RoomImage> images;
    @OneToMany
    List<CouponRoom> couponRooms;
}
