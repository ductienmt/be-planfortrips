package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "room_name", length = 100)
    String roomName;

    @Column(name = "price", precision = 10, scale = 2)
    BigDecimal price;

    @ManyToOne()
    @JoinColumn(name = "hotel_id")
    @JsonBackReference
    Hotel hotel;

    @Enumerated(EnumType.STRING)
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

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonManagedReference
    List<RoomImage> images;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    List<CouponRoom> couponRooms;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    List<RoomAmenities> roomAmenities;
}
