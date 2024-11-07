package com.be_planfortrips.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id")
    AccountEnterprise accountEnterprise;

    @Column(name = "name")
    String name;

    @Column(name = "address", nullable = false, length = Integer.MAX_VALUE)
    String address;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "description", length = Integer.MAX_VALUE)
    String description;

    @Column(name = "rating")
    int rating;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "hotel_images",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns =  @JoinColumn(name = "image_id")
    )
    @JsonManagedReference
    List<Image> images;
    @OneToMany(mappedBy = "hotel",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JsonManagedReference
    List<Room> rooms;
    // Danh sách User đã từng đặt khách sạn này
//    @ManyToMany
//    @JoinTable(
//            name = "hotel_users",
//            joinColumns = @JoinColumn(name = "hotel_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    List<User> usersUsed;
}
