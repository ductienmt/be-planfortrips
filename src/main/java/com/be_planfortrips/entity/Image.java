package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "url", length = Integer.MAX_VALUE)
    String url;

    @OneToMany(cascade = CascadeType.REMOVE)
    List<HotelImage> hotelImages;

    @OneToMany(cascade = CascadeType.REMOVE)
    List<RoomImage> roomImages;

}
