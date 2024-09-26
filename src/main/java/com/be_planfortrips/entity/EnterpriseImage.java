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

public class EnterpriseImage {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @ManyToOne @JoinColumn(name = "enterprise_id")AccountEnterprise accountEnterprise;
    @ManyToOne @JoinColumn(name = "image_id") Image image;
}
