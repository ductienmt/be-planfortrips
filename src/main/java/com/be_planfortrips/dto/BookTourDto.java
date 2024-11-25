package com.be_planfortrips.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookTourDto {

    Integer tourId;
    List<Integer> scheduleSeatOriginId;
    List<Integer> scheduleSeatDesId;

    List<Integer> roomId;

}
