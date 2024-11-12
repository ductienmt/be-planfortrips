package com.be_planfortrips.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackDto {

     @NotBlank(message = "Content cannot be blank")
     String content;

     @NotNull(message = "Rating is required")
     @Min(value = 1, message = "Rating must be at least 1")
     @Max(value = 5, message = "Rating must not exceed 5")
     Integer rating;

     @NotNull(message = "User ID is required")
     Long userId;

     @NotNull(message = "Account Enterprise ID is required")
     Long accountEnterpriseId;
}
