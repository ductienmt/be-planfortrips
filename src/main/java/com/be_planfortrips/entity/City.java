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

public class City {
    @Id
    @Column(length = 20)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", columnDefinition = "varchar(20)")
    Area area;

    @Column(length = 50)
    String nameCity;

    @Column(name = "description", length = Integer.MAX_VALUE)
    String description;

    @OneToMany
    List<Checkin> checkins;
}
