package com.be_planfortrips.dto;

import lombok.Value;

import java.io.Serializable;


@Value
public class RoomAmenitiesDto implements Serializable {
    Integer id;
    String name;
    String description;
    Boolean status;
    ImageDto icon;
    RoomDto room;
}