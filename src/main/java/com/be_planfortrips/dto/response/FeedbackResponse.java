package com.be_planfortrips.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackResponse {

    UUID id;
    String content;
    Integer rating;
    String userName;
    String typeEnterpriseName;
    String createdAt;
}
