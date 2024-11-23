package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.City;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AreaResponse {
    String id;
    String name;
    String description;
    List<City> cities;
}
