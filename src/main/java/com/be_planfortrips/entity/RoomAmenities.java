package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_amenities")
public class RoomAmenities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "fee", precision = 10, scale = 2)
    private BigDecimal fee;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "icon_id")
    private Image icon;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room room;
}
