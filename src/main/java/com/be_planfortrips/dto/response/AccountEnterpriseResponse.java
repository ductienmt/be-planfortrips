package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.City;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

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
    String representative;
    String typeEnterpriseDetailName;
    LocalDateTime createAt;
    String cityName;
    boolean status;
    String urlImage;


}
