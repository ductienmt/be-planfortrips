package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.RouteDTO;
import com.be_planfortrips.dto.response.RouteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IRouteService {
    RouteResponse createRoute(RouteDTO routeDTO) throws Exception;
    RouteResponse updateRoute(String id,RouteDTO routeDTO) throws Exception;
    Page<RouteResponse> getRoutes(PageRequest request);
    RouteResponse getByRouteId(String id) throws Exception;
    void deleteRouteById(String id);
}
