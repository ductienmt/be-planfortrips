package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "checkins")
public class Checkin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", columnDefinition = "varchar(20)")
    City city;

    @Column(length = 200)
    String name;

    @Column(name = "address", length = Integer.MAX_VALUE)
    String address;

    @Column(name = "latitude", precision = 9, scale = 6)
    BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    BigDecimal longitude;

    @Column(name = "payFee", precision = 10, scale = 2)
    BigDecimal payFee;
}