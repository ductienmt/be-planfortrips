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

public class City {
    @Id@Column(columnDefinition = "varchar(20)") String id;@ManyToOne@JoinColumn(name = "area_id",columnDefinition = "varchar(20)") Area area;
    @Column(columnDefinition = "varchar(50)") String nameCity;@Column(columnDefinition = "text") String description;
    @Column(columnDefinition = "decimal(0,6)") Double latitude;
    @Column(columnDefinition = "decimal(9,6)") Double longitude;
    @OneToMany
    List<Checkin> checkins;
}
