package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.VnPayDTO;
import com.be_planfortrips.dto.response.VnpPayResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Map;

public interface IVnPayService {
    VnpPayResponse createPayment(
            VnPayDTO vnPayDTO,
            HttpServletRequest httpServletRequest
    ) throws IOException;
    VnpPayResponse createPaymentForPlan(
            Integer planId,
            Integer departureId,
            Integer returnId,
            Integer bookingId,
            HttpServletRequest httpServletRequest
    ) throws IOException;
    String returnPage(Map<String, String> requestParams) throws IOException;
    String returnPageForPlan(Map<String, String> requestParams) throws IOException;
}
