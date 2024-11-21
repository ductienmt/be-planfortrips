package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.TourDTO;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.HotelMapper;
import com.be_planfortrips.mappers.impl.TourMapper;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.services.interfaces.ITourService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TourService implements ITourService {
    TourRepository tourRepository;
    TagRepository tagRepository;
    HotelRepository hotelRepository;
    RoomRepository roomRepository;
    CarCompanyRepository carCompanyRepository;
    ScheduleRepository scheduleRepository;
    AdminRepository adminRepository;
    CheckinRepository checkinRepository;
    CityRepository cityRepository;
    TourMapper tourMapper;
    @Override
    @Transactional
    public TourResponse createTour(TourDTO TourDTO) throws Exception {
        Tour tour = tourMapper.toEntity(TourDTO);

        Admin admin = adminRepository.findByUsername(TourDTO.getAdminUsername());
        if(admin==null) throw new AppException(ErrorType.notFound);
        Hotel hotelExisting = hotelRepository.findById(TourDTO.getHotelId())
                .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        CarCompany carExisting = carCompanyRepository.findById(TourDTO.getCarCompanyId())
                .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        Checkin checkinExisting = checkinRepository.findById(TourDTO.getCheckinId())
                .orElseThrow(()-> {throw new AppException(ErrorType.notFound);});
        City cityDepart = cityRepository.findById(TourDTO.getCityDepartId())
                .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        City cityArrive = cityRepository.findById(TourDTO.getCityArriveId())
                .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        List<Tag> tags = TourDTO.getTagNames()!= null
                ? tagRepository.findAllByNameIn(TourDTO.getTagNames())
                :new ArrayList<>();
        tour.setTags(tags);
        tour.setAdmin(admin);
        tour.setHotel(hotelExisting);
        tour.setCarCompany(carExisting);
        tour.setCheckin(checkinExisting);
        tour.setCityArrive(cityArrive);
        tour.setCityDepart(cityDepart);
        tourRepository.save(tour);

        TourResponse tourResponse = tourMapper.toResponse(tour);
        tourResponse.setHotel(hotelExisting);
        tourResponse.setCarCompany(carExisting);
        tourResponse.setAdminUsername(admin.getUserName());
        return tourResponse;
    }

    @Override
    @Transactional
    public TourResponse updateTour(Integer id, TourDTO TourDTO) throws Exception {
        Tour tour = tourMapper.toEntity(TourDTO);

        Admin admin = adminRepository.findByUsername(TourDTO.getAdminUsername());
        if(admin==null) throw new AppException(ErrorType.notFound);
        Hotel hotelExisting = hotelRepository.findById(TourDTO.getHotelId())
                .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        CarCompany carExisting = carCompanyRepository.findById(TourDTO.getCarCompanyId())
                .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        Checkin checkinExisting = checkinRepository.findById(TourDTO.getCheckinId())
                .orElseThrow(()-> {throw new AppException(ErrorType.notFound);});
        City cityDepart = cityRepository.findById(TourDTO.getCityDepartId())
                .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        City cityArrive = cityRepository.findById(TourDTO.getCityArriveId())
                .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        List<Tag> tags = TourDTO.getTagNames()!= null
                ? tagRepository.findAllByNameIn(TourDTO.getTagNames())
                :new ArrayList<>();
        tour.setId(id);
        tour.setTags(tags);
        tour.setAdmin(admin);
        tour.setHotel(hotelExisting);
        tour.setCarCompany(carExisting);
        tour.setCheckin(checkinExisting);
        tour.setCityArrive(cityArrive);
        tour.setCityDepart(cityDepart);
        tourRepository.save(tour);

        TourResponse tourResponse = tourMapper.toResponse(tour);
        tourResponse.setHotel(hotelExisting);
        tourResponse.setCarCompany(carExisting);
        tourResponse.setAdminUsername(admin.getUserName());
        return tourResponse;
    }

    @Override
    public Page<TourResponse> getActiveTours(PageRequest request, String title, Integer rating, List<String> tags) {
        Page<Tour> tours;
        if (title == null && (tags == null || tags.isEmpty()) && rating == null) {
            tours = tourRepository.findAllByActive(request);
        } else {
            tours = tourRepository.searchTours(request, title, rating, tags);
        }

        return tours.map(tour -> tourMapper.toResponse(tour)
        );
    }

    @Override
    public TourResponse getByTourId(Integer id) throws Exception {
        Tour tourExisting = tourRepository.findById(id).orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        return tourMapper.toResponse(tourExisting);
    }

    @Override
    @Transactional
    public void deleteTourById(Integer id) {
        Optional<Tour> tourOptional = tourRepository.findById(id);
        Tour tour = tourOptional.get();
        tour.setActive(false);
        tourOptional.ifPresent(tour1 -> tourRepository.save(tour));
    }
}
