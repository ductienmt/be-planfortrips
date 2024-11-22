package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.City;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountEnterpriseResponse {

    Long accountEnterpriseId;
    String enterpriseName;
    String phoneNumber;
    String email;
    String address;
    String taxCode;
    Long typeEnterpriseDetailId;
    String cityName;
    boolean status;

}
