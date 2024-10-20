package com.be_planfortrips.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeEnterpriseDetailResponse {

    Long id;
    String name;
    String description;
    Long typeEnterpriseId;
}
