package com.be_planfortrips.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BannerDto {
    String name;
    String forwardLink;
    Boolean isActive;
}
