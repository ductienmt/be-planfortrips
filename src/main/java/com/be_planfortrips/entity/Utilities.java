package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Table(name = "utilities")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Utilities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", length = 50)
    String name;

    @Column(name = "apply")
    Boolean apply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    TicketClass ticketClass;
}
