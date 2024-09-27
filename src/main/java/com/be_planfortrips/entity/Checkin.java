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

public class Checkin extends BaseEntity{
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)Long id;
    @ManyToOne@JoinColumn(name = "image_id",columnDefinition = "varchar(20)")Image image;
    @ManyToOne@JoinColumn(name = "city_id",columnDefinition = "varchar(20)") City city;
    @Column(columnDefinition = "varchar(200)") String name;
    @Column(columnDefinition = "text") String address;
    Double latitude;
    Double longitude;
    Double payFee;
}
