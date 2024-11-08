package com.be_planfortrips.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BannerDto {
    @NotBlank(message = "Vui lòng nhập tên banner.")
    String name;
    @NotBlank(message = "Vui lòng nhập link ảnh.")
    String forwardLink;
    Boolean isActive;
}
