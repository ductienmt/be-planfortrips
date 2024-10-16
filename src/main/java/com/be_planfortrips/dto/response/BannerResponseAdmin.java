package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Image;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BannerResponseAdmin {
    String name;
    Image image;
    String forwardLink;
    Boolean isActive;
}
