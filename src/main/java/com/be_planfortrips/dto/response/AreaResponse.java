package com.be_planfortrips.dto.response;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AreaResponse {
    String id;
    String name;
    String description;
}
