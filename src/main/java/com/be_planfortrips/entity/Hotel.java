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

public class Hotel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id")
    AccountEnterprise accountEnterprise;

    @Column(name = "address", nullable = false, length = Integer.MAX_VALUE)
    String address;

    @Column(name = "phone_number", nullable = false, length = 15)
    String phoneNumber;

    @Column(name = "description", length = Integer.MAX_VALUE)
    String description;

    @Column(name = "rating")
    int rating;

    @OneToMany(cascade = CascadeType.REMOVE)
    List<HotelImage> hotelImages;
}
