package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeEnterpriseDetailDto {
    String idTypeEnterprise;
    String name;
    String description;
}
