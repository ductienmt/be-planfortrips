package com.be_planfortrips.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataEssentialPlan {
    @NotBlank(message = "Vui lòng nhập địa điểm")
    String location;
    @NotBlank(message = "Vui lòng nhập địa điểm đến")
    String destination;
    @NotBlank(message = "Vui lòng nhập ngày bắt đầu")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime startDate;
    @NotBlank(message = "Vui lòng nhập ngày kết thúc")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime endDate;
    @NotBlank(message = "Vui lòng nhập số người")
    Integer numberPeople;
    @NotBlank(message = "Vui lòng nhập ngân sách")
    BigDecimal budget;
}
