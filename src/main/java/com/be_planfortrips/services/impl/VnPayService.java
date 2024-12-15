package com.be_planfortrips.services.impl;

import com.be_planfortrips.configs.VnPayConfig;
import com.be_planfortrips.dto.VnPayDTO;
import com.be_planfortrips.dto.response.VnpPayResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.services.interfaces.IVnPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class VnPayService implements IVnPayService {
    TicketRepository ticketRepository;
    ScheduleSeatRepository scheduleSeatRepository;
    BookingHotelRepository bookingHotelRepository;
    BookingHotelDetailRepository bookingHotelDetailRepository;
    PlanRepository planRepository;
    RoomRepository roomRepository;
    @Override
    @Transactional
    public VnpPayResponse createPayment(
            VnPayDTO vnPayDTO,
            HttpServletRequest httpServletRequest) throws IOException {
            try {
                List<Ticket> optionalTicket = ticketRepository.findAllById(vnPayDTO.getTicketId());
                List<BookingHotel> optionalBookingHotel = bookingHotelRepository.findAllById((vnPayDTO.getBookingId()));
                String ticketIds = optionalTicket.isEmpty()
                        ? "0"
                        : optionalTicket.stream()
                        .map(ticket -> String.valueOf(ticket.getId()))
                        .collect(Collectors.joining(", "));

                String bookingIds = optionalBookingHotel.isEmpty()
                        ? "0"
                        : optionalBookingHotel.stream()
                        .map(bookingHotel -> String.valueOf(bookingHotel.getBookingHotelId()))
                        .collect(Collectors.joining(", "));
                if (optionalTicket.size() == 0 && optionalBookingHotel.size() == 0) {
                    throw new AppException(ErrorType.TicketOrBookingHotelIsRequired);
                }
                String orderType = "other";
                String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
                String vnp_IpAddr = getClientIp(httpServletRequest);

                Map<String, String> vnp_Params = new HashMap<>();
                vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
                vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
                vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
//              vnp_Params.put("vnp_Amount", String.valueOf(ticket.getTotalPrice().multiply(BigDecimal.valueOf(100)).intValue()));
                vnp_Params.put("vnp_Amount", String.valueOf(vnPayDTO.getAmount().multiply(BigDecimal.valueOf(100)).intValue()));

                vnp_Params.put("vnp_CurrCode", "VND");

                vnp_Params.put("vnp_BankCode", vnPayDTO.getBankCode());
                vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
                vnp_Params.put("vnp_OrderInfo",
                        "Thanh toan don hang: " + vnp_TxnRef
                                + " Ticket: [" + ticketIds + "]"
                                + " Booking: [" + bookingIds + "]"
                );
                vnp_Params.put("vnp_OrderType", orderType);

                vnp_Params.put("vnp_Locale", "vn");
                vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
                vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

                Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                String vnp_CreateDate = formatter.format(cld.getTime());
                vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

                cld.add(Calendar.MINUTE, 15);
                String vnp_ExpireDate = formatter.format(cld.getTime());
                vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

                List fieldNames = new ArrayList(vnp_Params.keySet());
                Collections.sort(fieldNames);
                StringBuilder hashData = new StringBuilder();
                StringBuilder query = new StringBuilder();
                Iterator itr = fieldNames.iterator();
                while (itr.hasNext()) {
                    String fieldName = (String) itr.next();
                    String fieldValue = (String) vnp_Params.get(fieldName);
                    if ((fieldValue != null) && (fieldValue.length() > 0)) {
                        //Build hash data
                        hashData.append(fieldName);
                        hashData.append('=');
                        hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                        //Build query
                        query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                        query.append('=');
                        query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                        if (itr.hasNext()) {
                            query.append('&');
                            hashData.append('&');
                        }
                    }
                }
                String queryUrl = query.toString();
                String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
                queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
                String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

                VnpPayResponse vnpPayResponse = new VnpPayResponse();
                vnpPayResponse.setStatus("00");
                vnpPayResponse.setMessage("Successfully");
                vnpPayResponse.setUrl(paymentUrl);
                return vnpPayResponse;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
    }

    @Override
    public VnpPayResponse createPaymentForPlan(Integer planId, Integer departureId,
                                               Integer returnId,
                                               Integer bookingId, HttpServletRequest httpServletRequest) throws IOException {
        try {
            Plan plan  = planRepository.findById(Long.valueOf(planId))
                    .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
            String orderType = "other";
            String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
            String vnp_IpAddr = getClientIp(httpServletRequest);

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(plan.getFinalPrice().multiply(BigDecimal.valueOf(100)).intValue()));

            vnp_Params.put("vnp_CurrCode", "VND");

//            vnp_Params.put("vnp_BankCode", "NCB");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//            vnp_Params.put("vnp_OrderInfo",
//                    "Thanh toan don hang: " + vnp_TxnRef
//                            + " Plan: "+ plan.getId()
//            );
            vnp_Params.put("vnp_OrderInfo",
                    "Thanh toan don hang: " + vnp_TxnRef
                            + " Plan: " + plan.getId()
                            + " Departure: " + (departureId != null ? departureId : "null")
                            + " Return: " + (returnId != null ? returnId : "null")
                            + " Booking: " + (bookingId != null ? bookingId : "null")
            );

            vnp_Params.put("vnp_OrderType", orderType);

            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrlPlan);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

            VnpPayResponse vnpPayResponse = new VnpPayResponse();
            vnpPayResponse.setStatus("00");
            vnpPayResponse.setMessage("Successfully");
            vnpPayResponse.setUrl(paymentUrl);
            return vnpPayResponse;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public String returnPage(Map<String, String> requestParams) throws IOException {
        VnPayConfig.hashAllFields(requestParams);
        Pattern pattern = Pattern.compile("Ticket:\\s*\\[(.*?)\\]\\s*Booking:\\s*\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(requestParams.get("vnp_OrderInfo"));
        String ticketIds = "";
        String bookingIds = "";
        if (matcher.find()) {
             ticketIds = matcher.group(1);
             bookingIds = matcher.group(2);
        } else {
            System.out.println("Không tìm thấy ticket id và booking id trong chuỗi.");
        }
        List<Integer> ticketIdList = Arrays.stream(ticketIds.split(","))
                .map(String::trim)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        List<Long> bookingIdList = Arrays.stream(bookingIds.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());
        List<Ticket> optionalTicket = ticketRepository.findAllById(ticketIdList);
        List<BookingHotel> optionalBookingHotel = bookingHotelRepository.findAllById(bookingIdList);
        if (optionalTicket.isEmpty() && optionalBookingHotel.isEmpty()) {
            throw new AppException(ErrorType.CarCompanyHaveNotSeatAvailable);
        }
        if ("00".equals(requestParams.get("vnp_ResponseCode")) && "00".equals(requestParams.get("vnp_TransactionStatus"))) {
            for (Ticket t: optionalTicket){
                if (t != null) {
                    t.setStatus(Status.Complete);
                    ticketRepository.save(t);
                    List<ScheduleSeat> scheduleSeats = scheduleSeatRepository.findByScheduleId(t.getSchedule().getId());
                    for (ScheduleSeat scheduleSeat : scheduleSeats) {
                        for (Seat seat : t.getSeats()) {
                            if (scheduleSeat.getSeat().getId().equals(seat.getId())) {
                                scheduleSeat.setStatus(StatusSeat.Full);
                                scheduleSeatRepository.save(scheduleSeat);
                            }
                        }
                    }
                }
            }
            for(BookingHotel bookingHotel: optionalBookingHotel){
                if(bookingHotel != null){
                    bookingHotel.setStatus(Status.Complete);
                    bookingHotelRepository.save(bookingHotel);
                    List<BookingHotelDetail> bookingHotelDetails = bookingHotelDetailRepository
                            .findByBookingHotelBookingHotelId(bookingHotel.getBookingHotelId());
                    for(BookingHotelDetail bookingHotelDetail: bookingHotelDetails){
                        Room room = roomRepository.findById(bookingHotelDetail.getRoom().getId())
                                .orElseThrow(()-> new AppException(ErrorType.notFound));
                        room.setAvailable(false);
                        roomRepository.save(room);
                    }
                }
            }
            return "00";
        } else {
            // cần có tài khoản đối tác vnpay
//            Map<String, String> params = new HashMap<>();
//            params.put("vnp_RequestId", getRandomNumber(8));
//            params.put("vnp_Version", vnp_Version);
//            params.put("vnp_Command", "refund");
//            params.put("vnp_TmnCode", vnp_TmnCode);
//            params.put("vnp_TransactionType",requestParams.get("vnp_TransactionType") );
//            params.put("vnp_TxnRef", requestParams.get("vnp_TxnRef"));
//            params.put("vnp_Amount", requestParams.get("vnp_Amount"));
//            params.put("vnp_OrderInfo", requestParams.get("vnp_OrderInfo"));
//            params.put("vnp_TransactionDate", requestParams.get("vnp_TransactionDate"));
//            params.put("vnp_CreateBy", requestParams.get("vnp_CreateBy")!= null ? requestParams.get("vnp_CreateBy") : "");
//            params.put("vnp_CreateDate", requestParams.get("vnp_CreateDate"));
//            params.put("vnp_IpAddr", requestParams.get("vnp_IpAddr"));
//
//            String checksum = createChecksum(params, secretKey);
//            params.put("vnp_SecureHash", checksum);
//
//            String jsonInputString = new Gson().toJson(params);
//
//            // Thực hiện yêu cầu POST
//            URL url = new URL(vnp_ApiUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json; utf-8");
//            conn.setRequestProperty("Accept", "application/json");
//            conn.setDoOutput(true);
//
//            try (OutputStream os = conn.getOutputStream()) {
//                byte[] input = jsonInputString.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                ticket.setStatus(Status.Pending);
//                ticketRepository.save(ticket);
//            } else {
//                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
//            }
            return "02";
        }
    }

    @Override
    @Transactional
    public String returnPageForPlan(Map<String, String> requestParams) throws IOException {
        VnPayConfig.hashAllFields(requestParams);
//        Pattern pattern = Pattern.compile("Plan: (\\d+)");
//        Matcher matcher = pattern.matcher(requestParams.get("vnp_OrderInfo"));
        Pattern pattern = Pattern.compile("Plan: (\\d+) Departure: (\\d+) Return: (\\w+) Booking: (\\w+)");
        Matcher matcher = pattern.matcher(requestParams.get("vnp_OrderInfo"));
        String planId = "";
        String departureId = "";
        String returnId = "";
        String bookingId = "";
        if (matcher.find()) {
            planId = matcher.group(1);
            departureId = matcher.group(2);
            returnId = matcher.group(3).equals("null") ? null : matcher.group(3);
            bookingId = matcher.group(4).equals("null") ? null : matcher.group(4);
        } else {
            throw new RuntimeException("Không tìm thấy plan id trong chuỗi.");
        }
        Plan plan = planRepository.findById(Long.parseLong(planId))
                .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        if (plan == null) {
            throw new AppException(ErrorType.notFound);
        }
        if ("00".equals(requestParams.get("vnp_ResponseCode")) && "00".equals(requestParams.get("vnp_TransactionStatus"))) {
                if (plan != null) {
                    plan.setStatusPayment(Status.Complete);
                    planRepository.save(plan);
                }
            if (departureId != null) {
                Ticket departureTicket = ticketRepository.findById(Integer.parseInt(departureId))
                        .orElseThrow(() -> new AppException(ErrorType.notFound));
                departureTicket.setStatus(Status.Complete);
                ticketRepository.save(departureTicket);
            }

            if (returnId != null) {
                Ticket returnTicket = ticketRepository.findById(Integer.parseInt(returnId))
                        .orElseThrow(() -> new AppException(ErrorType.notFound));
                returnTicket.setStatus(Status.Complete);
                ticketRepository.save(returnTicket);
            }

            if (bookingId != null) {
                BookingHotel booking = bookingHotelRepository.findById(Long.parseLong(bookingId))
                        .orElseThrow(() -> new AppException(ErrorType.notFound));
                booking.setStatus(Status.Complete);
                bookingHotelRepository.save(booking);
            }
            return "00";
        } else {
            // cần có tài khoản đối tác vnpay
//            Map<String, String> params = new HashMap<>();
//            params.put("vnp_RequestId", getRandomNumber(8));
//            params.put("vnp_Version", vnp_Version);
//            params.put("vnp_Command", "refund");
//            params.put("vnp_TmnCode", vnp_TmnCode);
//            params.put("vnp_TransactionType",requestParams.get("vnp_TransactionType") );
//            params.put("vnp_TxnRef", requestParams.get("vnp_TxnRef"));
//            params.put("vnp_Amount", requestParams.get("vnp_Amount"));
//            params.put("vnp_OrderInfo", requestParams.get("vnp_OrderInfo"));
//            params.put("vnp_TransactionDate", requestParams.get("vnp_TransactionDate"));
//            params.put("vnp_CreateBy", requestParams.get("vnp_CreateBy")!= null ? requestParams.get("vnp_CreateBy") : "");
//            params.put("vnp_CreateDate", requestParams.get("vnp_CreateDate"));
//            params.put("vnp_IpAddr", requestParams.get("vnp_IpAddr"));
//
//            String checksum = createChecksum(params, secretKey);
//            params.put("vnp_SecureHash", checksum);
//
//            String jsonInputString = new Gson().toJson(params);
//
//            // Thực hiện yêu cầu POST
//            URL url = new URL(vnp_ApiUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json; utf-8");
//            conn.setRequestProperty("Accept", "application/json");
//            conn.setDoOutput(true);
//
//            try (OutputStream os = conn.getOutputStream()) {
//                byte[] input = jsonInputString.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                ticket.setStatus(Status.Pending);
//                ticketRepository.save(ticket);
//            } else {
//                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
//            }
            return "02";
        }
    }
    public String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress;
    }
 }
