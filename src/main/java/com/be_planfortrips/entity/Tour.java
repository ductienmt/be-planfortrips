package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="tours")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    String description;
    String destination;
    Integer numberPeople;
    Double rating;
    Double totalPrice;
    Integer day;
    Integer night;
    boolean isActive;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    Hotel hotel;

    @OneToOne
    @JoinColumn(name = "schedule_id")
    Schedule schedule;

    String note;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    Admin admin;

    @ManyToMany
    @JoinTable(name = "tour_tag",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns =@JoinColumn(name = "tag_id") )
    List<Tag> tags;

    @OneToMany
    List<Image> images;
}