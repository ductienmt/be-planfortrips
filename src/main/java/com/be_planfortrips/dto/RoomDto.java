package com.be_planfortrips.dto;

import com.be_planfortrips.entity.TypeOfRoom;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDto {

    @NotNull(message = "Availability is required")
    Boolean isAvailable;

    @Min(value = 1, message = "Maximum size must be at least 1")
    @Max(value = 1000, message = "Maximum size must not exceed 1000")
    Integer maxSize;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    BigDecimal price;

    @Min(value = 0, message = "Rating must be between 0 and 10")
    @Max(value = 10, message = "Rating must be between 0 and 10")
    Double rating;

    @NotNull(message = "Hotel ID is required")
    Long hotelId;

    @NotEmpty(message = "Room name is required")
    String roomName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description;

    @NotNull(message = "Room type is required")
    TypeOfRoom typeOfRoom;
}
