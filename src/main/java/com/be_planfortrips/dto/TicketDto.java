package com.be_planfortrips.dto;

import lombok.Data;

@Data
public class TicketDto {
    private Integer ticketId;
    private Integer seatId;
    private String ticketCode;
    private Double price;
}
