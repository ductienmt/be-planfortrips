package com.be_planfortrips.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeEnterpriseDetailDto {

    String name;
    String description;
    Long typeEnterpriseId;
}
