<<<<<<< HEAD
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
@Table(name = "airplanes")
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "air_plane_id")
    Long id;

    @Column(name = "model", length = 50)
    String model;

    @Column(name = "seat_capacity")
    int seatLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id")
    Airline airline;

    @OneToMany
    List<Flight> flights;

    @ManyToMany
    @JoinTable(
            name = "airplanes_users",
            joinColumns = @JoinColumn(name = "air_plane_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> usersUsed;
}
=======
//package com.be_planfortrips.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Builder
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Table(name = "airplanes")
//public class Airplane {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
//
//    @Column(name = "model", length = 50)
//    String model;
//
//    @Column(name = "seat_capacity")
//    int seatLimit;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "airline_id")
//    Airline airline;
//
//    @OneToMany(mappedBy = "airplane", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    List<Flight> flights;
//}
>>>>>>> a2b8a3077b0c703f7ee7dd0c64fe44094e080841
