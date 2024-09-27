package com.be_planfortrips.entity;

import jakarta.annotation.Nullable;
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

public class Area {
    @Id
    @Column(length = 20)
    String id;

    @Column(name = "name", length = 50)
    String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    String description;

    @OneToMany
    List<City> cities;
}
