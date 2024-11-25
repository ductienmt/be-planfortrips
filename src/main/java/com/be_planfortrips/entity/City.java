package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "cities")
public class City {
    @Id
    @Column(length = 20)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", columnDefinition = "varchar(20)")
    @JsonBackReference
    Area area;

    @Column(length = 50)
    String nameCity;

    @Column(name = "description", length = Integer.MAX_VALUE)
    String description;

    @OneToMany(mappedBy = "city", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    List<Checkin> checkins;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    @JsonBackReference
    Image image;
}
