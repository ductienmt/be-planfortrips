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

public class HotelImage {
    @Id
    Long id;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    Hotel hotel;
    @ManyToOne
    @JoinColumn(name = "image_id")
    Image image;
}
