package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Table(name = "plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan {

    @Id
    Integer planeId;

    @ManyToOne
    User user;

    // Id Enterprise
    Integer placeId;

    Integer vehicleId;


    // Id DetailService
    Integer placeDetailServiceId;

    Integer vehicleDetailServiceId;



}
