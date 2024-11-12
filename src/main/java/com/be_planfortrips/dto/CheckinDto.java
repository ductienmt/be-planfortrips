package com.be_planfortrips.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckinDto {
    @NotBlank(message = "Vui lòng nhập mã thành phố")
    String cityId;
    @NotBlank(message = "Vui lòng nhập tên điểm checkin")
    String name;
    @NotBlank(message = "Vui lòng nhập địa chỉ")
    String address;
    BigDecimal latitude;
    BigDecimal longitude;
    BigDecimal payFee;
}
