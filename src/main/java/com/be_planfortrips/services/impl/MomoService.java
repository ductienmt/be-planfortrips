package com.be_planfortrips.services.impl;

import com.be_planfortrips.configs.MomoConfig;
import com.be_planfortrips.dto.MomoDTO;
import com.be_planfortrips.repositories.TicketRepository;
import com.be_planfortrips.services.interfaces.IMomoService;
import com.be_planfortrips.utils.MomoEncoderUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.cloudinary.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class MomoService implements IMomoService {
    TicketRepository ticketRepository;
    @Override
    @Transactional
    public Map<String, Object> createPayment(MomoDTO momoDTO, HttpServletRequest httpServletRequest) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> momo_Params = new HashMap<>();
        String partnerCode = MomoConfig.PARTNER_CODE;
        String accessKey = MomoConfig.ACCESS_KEY;
        String secretKey = MomoConfig.SECRET_KEY;
        String returnUrl = MomoConfig.REDIRECT_URL;
        String notifyUrl = MomoConfig.NOTIFY_URL;
        momo_Params.put("partnerCode", partnerCode);
        momo_Params.put("accessKey", accessKey);
        momo_Params.put("requestId", String.valueOf(System.currentTimeMillis()));
        momo_Params.put("amount", String.valueOf(momoDTO.getAmount()));
        momo_Params.put("orderId", String.valueOf(momoDTO.getOrderId()));
        momo_Params.put("orderInfo", "Thanh toan don hang " +momoDTO.getOrderId());
        momo_Params.put("returnUrl", returnUrl);
        momo_Params.put("notifyUrl", notifyUrl);
        momo_Params.put("requestType", "captureMoMoWallet");

        String data = "partnerCode=" + partnerCode
                + "&accessKey=" + accessKey
                + "&requestId=" + momo_Params.get("requestId")
                + "&amount=" + momoDTO.getAmount()
                + "&orderId=" + momo_Params.get("orderId")
                + "&orderInfo=" + momo_Params.get("orderInfo")
                + "&returnUrl=" + returnUrl
                + "&notifyUrl=" + notifyUrl
                + "&extraData=";

        String hashData = MomoEncoderUtils.signHmacSHA256(data, secretKey);
        momo_Params.put("signature", hashData);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(MomoConfig.CREATE_ORDER_URL);
        StringEntity stringEntity = new StringEntity(momo_Params.toString());
        post.setHeader("content-type", "application/json");
        post.setEntity(stringEntity);

        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }
        JSONObject result = new JSONObject(resultJsonStr.toString());
        Map<String, Object> momoResponse = new HashMap<>();
        if (result.get("errorCode").toString().equalsIgnoreCase("0")) {
            momoResponse.put("requestType", result.get("requestType"));
            momoResponse.put("orderId", result.get("orderId"));
            momoResponse.put("payUrl", result.get("payUrl"));
            momoResponse.put("signature", result.get("signature"));
            momoResponse.put("requestId", result.get("requestId"));
            momoResponse.put("errorCode", result.get("errorCode"));
            momoResponse.put("message", result.get("message"));
            momoResponse.put("localMessage", result.get("localMessage"));
        } else {
            momoResponse.put("requestType", result.get("requestType"));
            momoResponse.put("orderId", result.get("orderId"));
            momoResponse.put("signature", result.get("signature"));
            momoResponse.put("requestId", result.get("requestId"));
            momoResponse.put("errorCode", result.get("errorCode"));
            momoResponse.put("message", result.get("message"));
            momoResponse.put("localMessage", result.get("localMessage"));
        }
        return momoResponse;
    }

    @Override
    public String returnPage(Map<String, String> requestParams) throws IOException {
        return null;
    }
}
