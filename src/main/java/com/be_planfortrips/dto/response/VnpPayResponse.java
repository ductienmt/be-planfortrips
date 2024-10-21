package com.be_planfortrips.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VnpPayResponse implements Serializable {
    String status;
    String message;
    String url;
}
