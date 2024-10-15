package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "url", length = Integer.MAX_VALUE)
    String url;

    @OneToMany(mappedBy = "image", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    List<RoomImage> roomImages;
}
