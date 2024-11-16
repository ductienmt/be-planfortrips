package com.be_planfortrips.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_id")
    Long tourId;

    String title;
    String description;
    Integer day;
    Integer night;
    Boolean isActive;
    Double rating;
    Integer numberPeople;

    @Column(name = "total_price")
    Double totalPrice;

    @ManyToOne
    CarCompany carCompany;

    @ManyToOne
    Hotel hotel;

    @ManyToOne
    City cityDestination;

    @ManyToOne
    City cityDeparture;

    @ManyToMany
    List<Checkin> checkinList;

    @ManyToMany
    @JoinTable(
            name = "tour_tags",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    List<Tag> tags;



}
