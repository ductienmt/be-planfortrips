package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.TourDto;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.dto.response.rsTourResponse.*;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.services.interfaces.ITourService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class TourServiceImpl implements ITourService{

    TourRepository tourRepository;
    CityRepository cityRepository;
    HotelRepository hotelRepository;
    CheckinRepository checkinRepository;
    CarCompanyRepository carCompanyRepository;
    TagRepository tagRepository;
    ScheduleRepository scheduleRepository;

    @Override
    public List<TourResponse> getAllTour(Integer page, Integer size) {
        if (page == null || size == null || page < 0 || size <= 0) {
            return tourRepository.findAll()
                    .stream()
                    .map(this::EntityToResponse)
                    .toList();
        }

        Pageable pageable = PageRequest.of(page, size);
        return tourRepository.findAll(pageable)
                .stream()
                .map(this::EntityToResponse)
                .toList();
    }


    @Override
    public TourResponse getTourById(Long tourId) {
        Tour tour = tourRepository.findById(tourId).orElseThrow(
                () -> new AppException(ErrorType.TourIdNotFound)
        );
        return null;
    }

    @Override
    public TourResponse createTour(TourDto tourDto) {
        Tour tour = RequestToEntity(tourDto);
        return EntityToResponse(tourRepository.save(tour));
    }

    @Override
    public void removeTourById(Long tourId) {
        Tour tour = tourRepository.findById(tourId).orElseThrow(
                () -> new AppException(ErrorType.TourIdNotFound)
        );
        tourRepository.deleteById(tourId);
    }

    @Override
    public TourResponse updateTourById(Long tourId, TourDto tourDto) {
        Tour tour = tourRepository.findById(tourId).orElseThrow(
                () -> new AppException(ErrorType.TourIdNotFound)
        );
        Tour tourNew = RequestToEntity(tourDto);
        tourNew.setTourId(tourId);
        return EntityToResponse(tourRepository.save(tourNew));
    }

    @Override
    public List<TourScheduleResponse> getScheduleAvailable(LocalDateTime day, String cityId) {
        // Xác định min (bắt đầu ngày) và max (kết thúc ngày)
        System.out.println(day);
        LocalDateTime min = day.toLocalDate().atStartOfDay();  // Bắt đầu ngày (00:00:00)
        LocalDateTime max = day.toLocalDate().atTime(23, 59, 59);  // Kết thúc ngày (23:59:59)
        System.out.println(min);
        System.out.println(max);
        List<TourScheduleBringData> scheduleBringData = tourRepository.getSchedulesByDate(min, max, cityId);

        List<TourScheduleResponse> responses = new ArrayList<>();
        scheduleBringData.stream().forEach((data) -> {
            TourScheduleResponse scheduleResponse = new TourScheduleResponse();
            Integer countSeat = scheduleRepository.getNumberSeatsEmpty(data.getScheduleId());
            scheduleResponse.setCountSeat(countSeat);
            scheduleResponse.setScheduleId(data.getScheduleId());
            scheduleResponse.setRouteId(data.getRouteId());
            scheduleResponse.setStationId(data.getStationId());
            scheduleResponse.setVehicleCode(data.getVehicleCode());

            responses.add(scheduleResponse);
        });

        return responses;
    }

    private Tour RequestToEntity(TourDto tourDto) {
        Tour tour = new Tour();
        tour.setDay(tourDto.getDay());
        tour.setDescription(tourDto.getDescription());
        tour.setNight(tourDto.getNight());
        tour.setIsActive(tourDto.getIsActive());
        tour.setNumberPeople(tourDto.getNumberPeople());
        tour.setTitle(tourDto.getTitle());
        // Chua co Id
        tour.setRating(null);

        // Check Destination
        City destination = cityRepository.findById(tourDto.getCityIdDestination())
                .orElseThrow(() -> new AppException(ErrorType.DestinationNotFound));
        // Check Departure
        City departure = cityRepository.findById(tourDto.getCityIdDeparture())
                .orElseThrow(() -> new AppException(ErrorType.DepartureNotFound));
        // Check CarCompany
        CarCompany carCompany = carCompanyRepository.findById(tourDto.getCarCompanyId())
                .orElseThrow(() -> new AppException(ErrorType.CarCompanyNotFound));
        // Check Hotel
        Hotel hotel = hotelRepository.findById(tourDto.getHotelId())
                .orElseThrow(() -> new AppException(ErrorType.HotelIdNotFound));
        // Check CheckIn
        List<Checkin> checkinList = tourDto.getCheckInIds().stream()
                .map(checkInId ->
                        checkinRepository.findById(checkInId)
                                .orElseThrow(() -> new AppException(ErrorType.CheckInIdNotFound))
                )
                .toList();
        // CheckTag
        List<Tag> tags = tourDto.getTagNames().stream()
                .map(tagName -> tagRepository.findByTagName(tagName)
                        .orElseThrow(() -> new AppException(ErrorType.TagIdNotFound))
                )
                .toList();

        tour.setCarCompany(carCompany);
        tour.setHotel(hotel);
        tour.setCityDeparture(departure);
        tour.setCityDestination(destination);
        tour.setCheckinList(checkinList);
        tour.setTags(tags);

        return tour;
    }

    private TourResponse EntityToResponse(Tour tour) {
        TourResponse response = new TourResponse();
        response.setTourId(tour.getTourId());
        response.setTitle(tour.getTitle());
        response.setDay(tour.getDay());
        response.setNight(tour.getNight());
        response.setIsActive(tour.getIsActive());
        response.setRating(tour.getRating());
        response.setNumberPeople(tour.getNumberPeople());
        response.setTotalPrice(tour.getTotalPrice());

        TourCarCompanyResponse companyResponse = new TourCarCompanyResponse();
        companyResponse.setCarCompanyId(tour.getCarCompany().getId());
        response.setCarCompany(companyResponse);

        TourHotelResponse hotelResponse = new TourHotelResponse();
        hotelResponse.setHotelId(tour.getHotel().getId());
        response.setHotel(hotelResponse);

        TourCityResponse destination = new TourCityResponse();
        destination.setCityId(tour.getCityDestination().getId());
        destination.setCityName(tour.getCityDestination().getNameCity());
        response.setDestination(destination);

        TourCityResponse departure = new TourCityResponse();
        departure.setCityId(tour.getCityDeparture().getId());
        departure.setCityName(tour.getCityDestination().getNameCity());
        response.setDeparture(departure);

        List<TourTagResponse> tourTagResponses =
                tour.getTags().stream()
                        .map(tag -> TourTagResponse.builder()
                                .name(tag.getTagName())
                                .id(tag.getTagId())
                                .description(tag.getTagDes())
                                .build()
                        )
                        .toList();
        response.setTags(tourTagResponses);

        return response;
    }
}
