package com.be_planfortrips.entity;
import jakarta.annotation.Nullable;
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

public class Area {
    @Id@Column(columnDefinition = "varchar(20)") String id;@Column(columnDefinition = "varchar(50)")
    String name;@Column(columnDefinition = "text")String description;
    @OneToMany
    List<City> cities;
}
