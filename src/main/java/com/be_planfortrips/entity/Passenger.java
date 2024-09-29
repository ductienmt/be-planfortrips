package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "passengers")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "fullname", length = 100)
    String passengerName;

    @Column(name = "birthdate")
    Date birthDate;

    @Column(name = "gender", length = 5)
    String gender;

    @Column(name = "citizencard_id")
    String citizenCardId;

    @Column(name = "phonenumber", length = 20)
    String phoneNumber;

    @Column(name = "email", length = 100)
    String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    BookingPlane bookingPlane;
}
