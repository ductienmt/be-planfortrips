package com.be_planfortrips.dto;

import lombok.Data;

@Data
public class RoomDto {
    private Integer id;
    private String roomName;
    private Double price;
    private Integer hotelId;
    private String typeOfRoom;
    private String description;
    private Integer maxSize;
    private Integer imageId;
    private String rating;
    private Boolean isAvailable;
}
