package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "type_enterprises")
public class TypeEnterprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Name type cannot be blank")
    @Size(max = 50, message = "Name type must be at most 50 characters")
    @Column(name = "name_type", length = 50)
    String nameType;

    @Size(max = 500, message = "Description must be at most 500 characters")
    @Column(name = "description", length = 500)
    String description;

}
