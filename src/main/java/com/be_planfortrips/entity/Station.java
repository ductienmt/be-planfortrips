package com.be_planfortrips.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "stations")
public class Station {
    @Id
    @ColumnDefault("nextval('stations_id_seq'::regclass)")
    @Column(name = "id", nullable = false)
    Integer id;

    @Column(name = "name", length = 100)
    String name;

    @Column(name = "city", length = 50)
    String city;

    @Column(name = "country", length = 50)
    String country;

}