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
    @ManyToOne
    @JoinColumn(name = "city_depart_id")
    City cityDepart;
    @ManyToOne
    @JoinColumn(name = "city_arrive_id")
    City cityArrive;
    Integer numberPeople;
    Double rating;
    Double totalPrice;
    Integer day;
    Integer night;
    boolean isActive;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "car_company_id")
    CarCompany carCompany;
    String note;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    Admin admin;
    @ManyToOne
    @JoinColumn(name = "checkin_id")
    Checkin checkin;
    @ManyToMany
    @JoinTable(name = "tour_tag",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns =@JoinColumn(name = "tag_id") )
    List<Tag> tags;

    @OneToMany
    List<Image> images;
}
