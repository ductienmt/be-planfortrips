package com.be_planfortrips.responses;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.HotelImage;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelResponse {
    String enterpriseName;
    String name;
    String address;
    String phoneNumber;
    String description;
    int rating;
    List<HotelImageResponse> hotelImageResponses ;
}
