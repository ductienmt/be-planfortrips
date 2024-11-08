package com.be_planfortrips.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AreaDto {
    @NotBlank(message = "Vui lòng nhập id.")
    String id;
    @NotBlank(message = "Vui lòng nhập tên.")
    String name;
    Boolean status;
    String description;
}
