package com.be_planfortrips.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanDto {
    @NotBlank(message = "Vui lòng nhập tên kế hoạch.")
    String planName;
    @NotBlank(message = "Vui lòng nhập ngày bắt đầu.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date startDate;
    @NotBlank(message = "Vui lòng nhập ngày kết thúc.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date endDate;
    @NotBlank(message = "Vui lòng nhập địa điểm.")
    String location;
    @NotBlank(message = "Vui lòng nhập địa điểm đến.")
    String destination;
    @NotBlank(message = "Vui lòng nhập ngân sách.")
    BigDecimal budget;
    @NotBlank(message = "Vui lòng nhập số người.")
    Integer numberPeople;
    @NotBlank(message = "Vui lòng nhập tổng giá.")
    BigDecimal totalPrice;
    List<PlanDetailDto> planDetails;
}
