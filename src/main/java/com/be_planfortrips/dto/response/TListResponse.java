package com.be_planfortrips.dto.response;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TListResponse <T> {
    List<T> listResponse;
    int totalPage;
}
