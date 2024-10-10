package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Column(name = "plate_number", length = 20)
    private String plateNumber;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "driver_name", length = 50)
    private String driverName;

    @Column(name = "driver_phone", length = 20)
    private String driverPhone;

    @ManyToOne
    @JoinColumn(name = "car_company_id")
    @JsonManagedReference
    private CarCompany carCompany;


    @Column(name = "type_vehicle")
    @Enumerated(EnumType.STRING)
    private TypeVehicle typeVehicle;

}