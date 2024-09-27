package com.be_planfortrips.dto;

import lombok.Data;

@Data
public class TicketClassDto {
    private Integer classId;
    private String className;
    private String description;
    private Integer classPrice;
}
