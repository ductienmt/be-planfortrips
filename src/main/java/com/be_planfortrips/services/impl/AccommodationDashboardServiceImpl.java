package com.be_planfortrips.services.impl;

import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.BookingHotelRepository;
import com.be_planfortrips.repositories.FeedbackRepository;
import com.be_planfortrips.services.interfaces.AccommodationDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccommodationDashboardServiceImpl implements AccommodationDashboardService {
    @Autowired
    private BookingHotelRepository bookingHotelRepository;
    @Autowired
    private TokenMapperImpl tokenMapperImpl;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private AccountEnterpriseRepository accountEnterpriseRepository;

    @Override
    public List<Map<String, Object>> getRevenue(String time) {
        Optional<AccountEnterprise> accountEnterprise = accountEnterpriseRepository.findById(tokenMapperImpl.getIdEnterpriseByToken());
        if (!accountEnterprise.isPresent()) {
            throw new AppException(ErrorType.notFound);
        }

        if (accountEnterprise.get().getTypeEnterpriseDetail().getId() != 4
                && accountEnterprise.get().getTypeEnterpriseDetail().getId() != 5
                && accountEnterprise.get().getTypeEnterpriseDetail().getId() != 5) {
            throw new RuntimeException("Bạn không phải là đối tác khách sạn");
        }

        if (time.equals("week")) {
            return bookingHotelRepository.compileRevenueWithWeek(tokenMapperImpl.getIdEnterpriseByToken());
        } else if (time.equals("month")) {
            return bookingHotelRepository.compileRevenueWithMonth(tokenMapperImpl.getIdEnterpriseByToken());
        } else if (time.equals("year")) {
            return bookingHotelRepository.compileRevenueWithYear(tokenMapperImpl.getIdEnterpriseByToken());
        } else {
            throw new AppException(ErrorType.inputFieldInvalid);
        }
    }

    @Override
    public Map<String, Object> getInfo() {
        Optional<AccountEnterprise> accountEnterprise = accountEnterpriseRepository.findById(tokenMapperImpl.getIdEnterpriseByToken());
        if (!accountEnterprise.isPresent()) {
            throw new AppException(ErrorType.notFound);
        }

        if (accountEnterprise.get().getTypeEnterpriseDetail().getId() != 4
                && accountEnterprise.get().getTypeEnterpriseDetail().getId() != 5
                && accountEnterprise.get().getTypeEnterpriseDetail().getId() != 5) {
            throw new RuntimeException("Bạn không phải là đối tác khách sạn");
        }
        Map<String, Object> response = new HashMap<>();
        Integer roomInUse = bookingHotelRepository.countRoomInUse(tokenMapperImpl.getIdEnterpriseByToken());
        Integer roomEmpty = bookingHotelRepository.countRoomNotUse(tokenMapperImpl.getIdEnterpriseByToken());
        Integer roomUsed = bookingHotelRepository.countRoomUsed(tokenMapperImpl.getIdEnterpriseByToken());
        Integer roomBookAdvance = bookingHotelRepository.countRoomFuture(tokenMapperImpl.getIdEnterpriseByToken());
        response.put("roomInUse", roomInUse);
        response.put("roomEmpty", roomEmpty);
        response.put("roomUsed", roomUsed);
        response.put("roomBookAdvance", roomBookAdvance);
        return response;
    }

    @Override
    public Map<String, Object> getFeedback() {
        Optional<AccountEnterprise> accountEnterprise = accountEnterpriseRepository.findById(tokenMapperImpl.getIdEnterpriseByToken());
        if (!accountEnterprise.isPresent()) {
            throw new AppException(ErrorType.notFound);
        }

        if (accountEnterprise.get().getTypeEnterpriseDetail().getId() != 4
                && accountEnterprise.get().getTypeEnterpriseDetail().getId() != 5
                && accountEnterprise.get().getTypeEnterpriseDetail().getId() != 5) {
            throw new RuntimeException("Bạn không phải là đối tác khách sạn");
        }
        return feedbackRepository.compileFeedbackByEnterprise(tokenMapperImpl.getIdEnterpriseByToken());
    }


}
