package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @Column(name = "code", nullable = false, length = 10)
    String code;

    @Column(name = "plate_number", length = 20)
    String plateNumber;

    @Column(name = "capacity")
    Integer capacity;

    @Column(name = "driver_name", length = 50)
    String driverName;

    @Column(name = "driver_phone", length = 20)
    String driverPhone;

    @ManyToOne
    @JoinColumn(name = "car_company_id")
    @JsonBackReference
    CarCompany carCompany;

    @Column(name = "type_vehicle")
    @Enumerated(EnumType.STRING)
    TypeVehicle typeVehicle;
    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    List<Seat> seats;


}