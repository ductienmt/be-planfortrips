package com.be_planfortrips.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "type_enterprise_details")
public class TypeEnterpriseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", length = 50)
    String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_typeEnterprise")
    TypeEnterprise typeEnterprise;
}
