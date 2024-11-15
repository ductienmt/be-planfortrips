package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.TourDTO;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
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
    TourMapper tourMapper;
    @Override
    @Transactional
    public TourResponse createTour(TourDTO TourDTO) throws Exception {
        Tour tour = tourMapper.toEntity(TourDTO);
        return saveOrUpdateTour(tour, TourDTO);
    }

    @Override
    @Transactional
    public TourResponse updateTour(Integer id, TourDTO TourDTO) throws Exception {
        Tour tourExisting = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        return saveOrUpdateTour(tourExisting, TourDTO);
    }

    @Override
    public Page<TourResponse> getTours(PageRequest request, String title, Integer rating, List<String> tags) {
        Page<Tour> tours;
        if (title == null && (tags == null || tags.isEmpty()) && rating == null) {
            tours = tourRepository.findAll(request);
        } else {
            tours = tourRepository.searchTours(request, title, rating, tags);
        }

        return tours.map(tour -> {
            TourResponse response = tourMapper.toResponse(tour);
            Hotel hotel = tour.getHotel();
            CarCompany carCompany = tour.getCarCompany();
            List<Room> rooms = roomRepository.findAvailableRooms(
                    LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), (hotel!=null)
                            ?hotel.getAccountEnterprise().getCity().getNameCity():"");
            List<ScheduleSeat> scheduleSeats =tour.getSchedule() != null? tour.getSchedule().getScheduleSeats().stream()
                    .filter(scheduleSeat -> scheduleSeat.getStatus().equals(StatusSeat.Empty))
                    .collect(Collectors.toList()) : null;

            return buildTourResponse(tour, hotel, rooms, carCompany, tour.getSchedule(), scheduleSeats, tour.getAdmin() != null ?tour.getAdmin().getUserName(): "" );
        });
    }

    @Override
    public TourResponse getByTourId(Integer id) throws Exception {
        Tour tourExisting = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound));

        Hotel hotel = tourExisting.getHotel();
        CarCompany carCompany = tourExisting.getCarCompany();
        Schedule schedule = tourExisting.getSchedule();

        LocalDateTime timeCheckout = schedule.getArrivalTime().plusDays(Math.max(tourExisting.getDay(), tourExisting.getNight()));
        List<Room> rooms = roomRepository.findAvailableRooms(
                        schedule.getArrivalTime().minusHours(6), timeCheckout.plusHours(6), tourExisting.getDestination())
                .stream()
                .filter(room -> room.getHotel().getId().equals(hotel.getId()))
                .collect(Collectors.toList());
        List<ScheduleSeat> scheduleSeats = schedule.getScheduleSeats().stream()
                .filter(scheduleSeat -> scheduleSeat.getStatus().equals(StatusSeat.Empty))
                .collect(Collectors.toList());
        return buildTourResponse(tourExisting, hotel, rooms, carCompany, schedule, scheduleSeats, tourExisting.getAdmin().getUserName());
    }


    @Override
    @Transactional
    public void deleteTourById(Integer id) {
        Optional<Tour> tourOptional = tourRepository.findById(id);
        Tour tour = tourOptional.get();
        tour.setActive(false);
        tourOptional.ifPresent(tour1 -> tourRepository.save(tour));
    }
    private TourResponse saveOrUpdateTour(Tour tour, TourDTO TourDTO) throws Exception {
        Admin admin = adminRepository.findByUsername(TourDTO.getAdminUsername());
        if (admin == null) throw new AppException(ErrorType.notFound);

        Schedule schedule = scheduleRepository.findById(TourDTO.getScheduleId())
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        Hotel hotelExisting = hotelRepository.findById(TourDTO.getHotelId())
                .orElseThrow(() -> new AppException(ErrorType.notFound));

        LocalDateTime timeCheckout = schedule.getArrivalTime().plusDays(Math.max(TourDTO.getDay(), TourDTO.getNight()));
        List<Room> rooms = roomRepository.findAvailableRooms(
                        schedule.getArrivalTime().minusHours(6), timeCheckout.plusHours(6), TourDTO.getDestination())
                .stream()
                .filter(room -> room.getHotel().getId().equals(hotelExisting.getId()))
                .collect(Collectors.toList());
        if (rooms.isEmpty()) throw new AppException(ErrorType.HotelHaveNotRoomAvailable);

        CarCompany carExisting = carCompanyRepository.findById(TourDTO.getCarCompanyId())
                .orElseThrow(() -> new AppException(ErrorType.notFound));

        List<ScheduleSeat> scheduleSeats = schedule.getScheduleSeats().stream()
                .filter(scheduleSeat -> scheduleSeat.getStatus().equals(StatusSeat.Empty))
                .collect(Collectors.toList());
        if (scheduleSeats.isEmpty()) throw new AppException(ErrorType.CarCompanyHaveNotSeatAvailable);

        List<Tag> tags = TourDTO.getTagNames() != null
                ? tagRepository.findAllByNameIn(TourDTO.getTagNames())
                : new ArrayList<>();

        tour.setTags(tags);
        tour.setAdmin(admin);
        tour.setHotel(hotelExisting);
        tour.setCarCompany(carExisting);
        tour.setSchedule(schedule);
        tourRepository.save(tour);

        return buildTourResponse(tour, hotelExisting, rooms, carExisting, schedule, scheduleSeats, admin.getUserName());
    }

    private TourResponse buildTourResponse(Tour tour, Hotel hotel, List<Room> rooms,
                                           CarCompany carCompany, Schedule schedule,
                                           List<ScheduleSeat> scheduleSeats, String adminUsername) {
        TourResponse tourResponse = tourMapper.toResponse(tour);
        tourResponse.setHotel(hotel);
        tourResponse.setRoomListAvailable(rooms);
        tourResponse.setCarCompany(carCompany);
        tourResponse.setSchedule(schedule);
        tourResponse.setScheduleSeatListAvailable(scheduleSeats);
        tourResponse.setAdminUsername(adminUsername);
        return tourResponse;
    }
}
