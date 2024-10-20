package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.MomoDTO;
import com.be_planfortrips.dto.VnPayDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface IMomoService {
    Map<String, Object> createPayment(
            MomoDTO momoDTO,
            HttpServletRequest httpServletRequest
    ) throws IOException, NoSuchAlgorithmException, InvalidKeyException;
    String returnPage(Map<String, String> requestParams) throws IOException;
}
