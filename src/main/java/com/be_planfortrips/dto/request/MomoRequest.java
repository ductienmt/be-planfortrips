package com.be_planfortrips.dto.request;

import lombok.Data;

@Data
public class MomoRequest {
    private Integer ticketId;
    private Integer bookingId;
    private String voucherCode;
    private String returnUrl;
    private String notifyUrl;
    private String orderInfo;
}
