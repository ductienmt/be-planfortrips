package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TicketClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id", nullable = false)
    Long id;

    @Column(name = "class_name", length = 50)
    String className;

    @Column(name = "description", length = Integer.MAX_VALUE)
    String description;

    @ColumnDefault("1")
    @Column(name = "class_price")
    int classPrice;
}
