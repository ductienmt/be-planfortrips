package com.be_planfortrips.services.interfaces;

import java.util.List;
import java.util.Map;

public interface TransportationDashboardService {
    List<Map<String, Object>> getRevenue(String time);

    Map<String, Object> getInfo();

    Map<String, Object> getFeedback();
}
