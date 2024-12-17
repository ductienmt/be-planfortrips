package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.RouteDTO;
import com.be_planfortrips.dto.response.RouteResponse;
import com.be_planfortrips.entity.Route;
import com.be_planfortrips.entity.Station;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.CityRepository;
import com.be_planfortrips.repositories.RouteRepository;
import com.be_planfortrips.repositories.StationRepository;
import com.be_planfortrips.services.interfaces.IRouteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RouteService implements IRouteService {
    RouteRepository routeRepository;
    StationRepository stationRepository;
    private final CityRepository cityRepository;
    private final TokenMapperImpl tokenMapperImpl;

    @Override
    @Transactional
    public RouteResponse createRoute(RouteDTO routeDTO) throws Exception {
        Optional<Route> existingRoute = routeRepository.findById(routeDTO.getId());
        if(existingRoute.isPresent()){
            throw new AppException(ErrorType.routeCodeExisted);
        }
        Station originStation = stationRepository.findById(routeDTO.getOriginStationId())
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        Station destinationStation = stationRepository.findById(routeDTO.getDestinationStationId())
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        if(!originStation.equals(destinationStation)){
            throw new AppException(ErrorType.routeExisted);
        }
        boolean routeExists = routeRepository.existsByOriginStationAndDestinationStationAndIdNot(
                originStation, destinationStation, routeDTO.getId()
        );
        if (routeExists) {
            throw new AppException(ErrorType.routeExisted);
        }
        Route route = Route.builder()
                .id(routeDTO.getId())
                .originStation(originStation)
                .destinationStation(destinationStation)
                .build();

        routeRepository.save(route);

        return RouteResponse.builder()
                .id(route.getId())
                .originStation(route.getOriginStation())
                .destinationStation(route.getDestinationStation())
                .build();
    }

    @Override
    @Transactional
    public RouteResponse updateRoute(String id, RouteDTO routeDTO) throws Exception {
        Route existingRoute = routeRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorType.routeCodeExisted)
        );

        Station originStation = stationRepository.findById(routeDTO.getOriginStationId())
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        Station destinationStation = stationRepository.findById(routeDTO.getDestinationStationId())
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        if(!originStation.equals(destinationStation)){
            throw new AppException(ErrorType.routeExisted);
        }
        boolean routeExists = routeRepository.existsByOriginStationAndDestinationStationAndIdNot(
                originStation, destinationStation, existingRoute.getId()
        );
        if (routeExists) {
            throw new AppException(ErrorType.routeExisted);
        }
        existingRoute = Route.builder()
                .id(routeDTO.getId())
                .originStation(originStation)
                .destinationStation(destinationStation)
                .build();

        routeRepository.save(existingRoute);

        return RouteResponse.builder()
                .id(existingRoute.getId())
                .originStation(existingRoute.getOriginStation())
                .destinationStation(existingRoute.getDestinationStation())
                .build();
    }


    @Override
    public Page<RouteResponse> getRoutes(PageRequest request,String originStationName,String desStationName) {
        return routeRepository.getRouteByOriginStation_NameAndDestinationStation_Name(request,originStationName,desStationName).map(route -> RouteResponse.builder()
                .id(route.getId())
                .originStation(route.getOriginStation())
                .destinationStation(route.getDestinationStation())
                .build());
    }

    @Override
    public RouteResponse getByRouteId(String id) throws Exception {
        Route existingRoute = routeRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorType.notFound));
        return RouteResponse.builder()
                .id(existingRoute.getId())
                .originStation(existingRoute.getOriginStation())
                .destinationStation(existingRoute.getDestinationStation())
                .build();
    }

    @Override
    public Map<String, Object> getCityByRouteId(String id) {
        Route route = routeRepository.findById(id).orElseThrow(() -> new AppException(ErrorType.notFound, "Không tiìm thấy route"));
        Map<String, Object> city = new HashMap<>();
        city.put("originalCity", route.getOriginStation().getCity().getNameCity());
        city.put("destination", route.getDestinationStation().getCity().getNameCity());
        return city;
    }

    @Override
    @Transactional
    public void deleteRouteById(String id) {
        Optional<Route> optionalRoute = routeRepository.findById(id);
        optionalRoute.ifPresent(routeRepository::delete);
    }

    @Override
    public List<RouteResponse> getRoutesByEnterpriseId() {
        List<Map<String, Object>> routes = routeRepository.getRouteRelevance(tokenMapperImpl.getIdEnterpriseByToken());
        List<RouteResponse> routeResponses = new ArrayList<>();
        for (Map<String, Object> route : routes) {
            String routeId = (String) route.get("route_id");
            String originCityId = (String) route.get("origin_city_id");
            String destinationCityId = (String) route.get("destination_city_id");

            Optional<Route> routeOptional = routeRepository.findById(routeId);

            // Fetch Station entities by city_id
            Station originStation = routeOptional.get().getOriginStation();
            if (originStation == null) throw new IllegalArgumentException("Origin station not found for city_id: " + originCityId);

            Station destinationStation = routeOptional.get().getDestinationStation();
            if (destinationStation == null) throw new IllegalArgumentException("Destination station not found for city_id: " + destinationCityId);

            RouteResponse routeResponse = RouteResponse.builder()
                    .id(routeId)
                    .originStation(originStation)
                    .destinationStation(destinationStation)
                    .build();
            routeResponses.add(routeResponse);
        }
        return routeResponses;
    }

}
