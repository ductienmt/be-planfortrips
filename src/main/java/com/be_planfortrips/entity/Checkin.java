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
    @Column(columnDefinition = "decimal(0,6)") Double latitude;
    @Column(columnDefinition = "decimal(9,6)") Double longitude;
    @Column(columnDefinition = "decimal(10,2)") Double payFee;
}
