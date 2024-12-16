package com.be_planfortrips.services.impl;

import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.services.interfaces.TransportationDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TransportationDashboardServiceImpl implements TransportationDashboardService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TokenMapperImpl tokenMapperImpl;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private CouponRepository couponRepository;
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

        if (accountEnterprise.get().getTypeEnterpriseDetail().getId() != 1) {
            throw new RuntimeException("Bạn không phải là đối tác vận tải");
        }
        if (time.equals("week")) return ticketRepository.compileRevenueWithWeek(tokenMapperImpl.getIdEnterpriseByToken());
        else if (time.equals("month")) return ticketRepository.compileRevenueWithMonth(tokenMapperImpl.getIdEnterpriseByToken());
        else if (time.equals("year")) return ticketRepository.compileRevenueWithYear(tokenMapperImpl.getIdEnterpriseByToken());
        else throw new RuntimeException("Invalid time");
    }

    @Override
    public Map<String, Object> getInfo() {
        Optional<AccountEnterprise> accountEnterprise = accountEnterpriseRepository.findById(tokenMapperImpl.getIdEnterpriseByToken());
        if (!accountEnterprise.isPresent()) {
            throw new AppException(ErrorType.notFound);
        }

        if (accountEnterprise.get().getTypeEnterpriseDetail().getId() != 1) {
            throw new RuntimeException("Bạn không phải là đối tác vận tải");
        }
        Map<String, Object> info = new HashMap<>();
        info.put("totalTicketBooked", ticketRepository.countTicketBooked(tokenMapperImpl.getIdEnterpriseByToken()));
        info.put("totalTicketBookAdvance", ticketRepository.countTicketBookAdvance(tokenMapperImpl.getIdEnterpriseByToken()));
        info.put("totalVehicleActive", vehicleRepository.countByEnterpriseId(tokenMapperImpl.getIdEnterpriseByToken()));
        info.put("totalVoucherActive", couponRepository.countByEnterpriseId(tokenMapperImpl.getIdEnterpriseByToken()));
        return info;
    }

    @Override
    public Map<String, Object> getFeedback() {
        Optional<AccountEnterprise> accountEnterprise = accountEnterpriseRepository.findById(tokenMapperImpl.getIdEnterpriseByToken());
        if (!accountEnterprise.isPresent()) {
            throw new AppException(ErrorType.notFound);
        }

        if (accountEnterprise.get().getTypeEnterpriseDetail().getId() != 1) {
            throw new RuntimeException("Bạn không phải là đối tác vận tải");
        }
        return feedbackRepository.compileFeedbackByEnterprise(tokenMapperImpl.getIdEnterpriseByToken());
    }
}
