package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Tour extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "route_id")
    Route route;
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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "tour_checkins",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "checkin_id"))
    @JsonManagedReference
    List<Checkin> checkin;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "tour_tag",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns =@JoinColumn(name = "tag_id") )
    List<Tag> tags;

    @OneToMany
    List<Image> images;

    @ManyToMany
    List<User> userUsed;
}
