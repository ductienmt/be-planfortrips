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
@Table(name = "hotels")
public class Hotel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    AccountEnterprise accountEnterprise;
    String name;
    @Column(name = "address", nullable = false, length = Integer.MAX_VALUE)
    String address;
    String phoneNumber;
    String description;
    int rating;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    List<HotelImage> hotelImages;

}
