package com.be_planfortrips.models;

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

    String url;
    @OneToMany(cascade = CascadeType.REMOVE)
    List<HotelImage> hotelImages;
    @OneToMany(cascade = CascadeType.REMOVE)
    List<RoomImage> roomImages;
    @OneToMany
    List<EnterpriseImage> enterpriseImages;

}
