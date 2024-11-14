package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.RouteDTO;
import com.be_planfortrips.dto.response.RouteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface IRouteService {
    RouteResponse createRoute(RouteDTO routeDTO) throws Exception;
    RouteResponse updateRoute(String id,RouteDTO routeDTO) throws Exception;
    Page<RouteResponse> getRoutes(PageRequest request,String originStationName,String desStationName);
    RouteResponse getByRouteId(String id) throws Exception;
    Map<String, Object> getCityByRouteId(String id);
    void deleteRouteById(String id);
}
