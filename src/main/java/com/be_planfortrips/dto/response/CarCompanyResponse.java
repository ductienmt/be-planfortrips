package com.be_planfortrips.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarCompanyResponse {

    String carCompanyName;
    Integer carCompanyId;
}
