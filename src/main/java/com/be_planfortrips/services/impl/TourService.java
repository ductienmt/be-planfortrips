package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.TourDTO;
import com.be_planfortrips.dto.response.*;
import com.be_planfortrips.dto.sql.TourDataSql;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.RoomAmenitiesMapper;
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
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
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
    RouteRepository routeRepository;
    ImageRepository imageRepository;
    TourMapper tourMapper;
    CloudinaryService cloudinaryService;
    private final ScheduleSeatRepository scheduleSeatRepository;
    RoomAmenitiesMapper roomAmenitiesMapper;
    private final AreaRepository areaRepository;

    @Override
    @Transactional
    public TourResponse createTour(TourDTO TourDTO) throws Exception {
        Tour tour = tourMapper.toEntity(TourDTO);

        Admin admin = adminRepository.findByUsername(TourDTO.getAdminUsername());
        Hotel hotelExisting = hotelRepository.findById(TourDTO.getHotelId())
                .orElseThrow(() -> {
                    throw new AppException(ErrorType.notFound);
                });
        CarCompany carExisting = carCompanyRepository.findById(TourDTO.getCarCompanyId())
                .orElseThrow(() -> {
                    throw new AppException(ErrorType.notFound);
                });
        List<Checkin> checkinExisting = new ArrayList<>();
        for(Long checkinId : TourDTO.getCheckinId()){
            Checkin checkin = checkinRepository.findById(checkinId).orElseThrow(
                    ()->{throw new AppException(ErrorType.notFound);}
            );
            checkinExisting.add(checkin);
        }
        Route route =  routeRepository.findById(TourDTO.getRouteId())
                .orElseThrow(()-> {throw new AppException(ErrorType.notFound);});
        List<Tag> tags = TourDTO.getTagNames()!= null
                ? tagRepository.findAllByNameIn(TourDTO.getTagNames())
                : new ArrayList<>();
        tour.setTags(tags);
        tour.setAdmin(admin);
        tour.setHotel(hotelExisting);
        tour.setCarCompany(carExisting);
        tour.setCheckin(checkinExisting);
        tour.setRoute(route);
        tourRepository.save(tour);

        TourResponse tourResponse = tourMapper.toResponse(tour);
        tourResponse.setHotel(hotelExisting);
        tourResponse.setCarCompany(carExisting);
        tourResponse.setRoute(route);
        tourResponse.setAdminUsername(admin.getUserName());
        return tourResponse;
    }

    @Override
    @Transactional
    public TourResponse updateTour(Integer id, TourDTO TourDTO) throws Exception {
        Tour tour = tourMapper.toEntity(TourDTO);

        Admin admin = new Admin();
        if(TourDTO.getAdminUsername()!=null){
           admin = adminRepository.findByUsername(TourDTO.getAdminUsername());
        }
        Hotel hotelExisting = hotelRepository.findById(TourDTO.getHotelId())
                .orElseThrow(() -> {
                    throw new AppException(ErrorType.notFound);
                });
        CarCompany carExisting = carCompanyRepository
                .findById(TourDTO.getCarCompanyId())
                .orElseThrow(() -> {
                    throw new AppException(ErrorType.notFound);
                });
        List<Checkin> checkinExisting = new ArrayList<>();
        for(Long checkinId : TourDTO.getCheckinId()){
            Checkin checkin = checkinRepository.findById(checkinId).orElseThrow(
                    ()->{throw new AppException(ErrorType.notFound);}
            );
            checkinExisting.add(checkin);
        }
        Route route =  routeRepository.findById(TourDTO.getRouteId())
                .orElseThrow(()-> {throw new AppException(ErrorType.notFound);});
        List<Tag> tags = TourDTO.getTagNames()!= null
                ? tagRepository.findAllByNameIn(TourDTO.getTagNames())
                : new ArrayList<>();
        tour.setId(id);
        tour.setTags(tags);
        tour.setAdmin(admin);
        tour.setHotel(hotelExisting);
        tour.setCarCompany(carExisting);
        tour.setCheckin(checkinExisting);
        tour.setRoute(route);
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
        Tour tourExisting = tourRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorType.notFound);
        });
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

    @Override
    public TourResponse uploadImage(Integer id, List<MultipartFile> files) throws Exception {
        Tour tour = tourRepository.findById(id).orElseThrow(
                () -> new Exception("Not found"));
        List<Image> imageList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getSize() == 0) {
                continue;
            }
            // Kiểm tra kích thước file và định dạng
            if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                new Exception("File is too large! Maximum size is 10MB");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                new Exception("File must be an image");
            }
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, "tours");
            String imageUrl = (String) uploadResult.get("secure_url");
            Image image = new Image();
            image.setUrl(imageUrl);
            image = imageRepository.saveAndFlush(image);
            imageList.add(image);
        }
        tour.getImages().addAll(imageList);
        tourRepository.saveAndFlush(tour);
        return tourMapper.toResponse(tour);
    }

    @Override
    @Transactional
    public TourResponse deleteImage(Integer id, List<Integer> imageIds) throws Exception {
        Tour Tour = tourRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorType.notFound));
        List<Image> images = Tour.getImages();
        List<Image> imagesToDelete = images.stream()
                .filter(image -> imageIds.contains(Integer.valueOf(String.valueOf(image.getId()))))
                .collect(Collectors.toList());
        if (imagesToDelete.isEmpty()) {
            throw new Exception("No images found to delete");
        }

        for (Image image : imagesToDelete) {
            try {
                String publicId = cloudinaryService.getPublicIdFromUrl(image.getUrl());
                cloudinaryService.deleteFile(publicId);
                Tour.getImages().remove(image);
                imageRepository.delete(image);
            } catch (Exception e) {
                throw new Exception("Error deleting image: " + e.getMessage());
            }
        }
        tourRepository.saveAndFlush(Tour);
        return tourMapper.toResponse(Tour);
    }

    @Override
    public TourDetailResponse getTourDetail(Integer tourId) {
        Tour tour = tourRepository.findById(tourId).orElseThrow(
                () -> new AppException(ErrorType.TourIdNotFound)
        );
        TourDetailResponse response = new TourDetailResponse();
        response.setDay(tour.getDay());
        response.setNight(tour.getNight());
        response.setDescription(tour.getDescription());
        response.setImages(tour.getImages());
        response.setRating(tour.getRating());
        response.setNumberPeople(tour.getNumberPeople());
        response.setTitle(tour.getTitle());
        response.setTags(tour.getTags());

        // HotelResponse
        Hotel hotel = hotelRepository.findById(tour.getHotel().getId()).orElseThrow();
        HotelResponse hotelResponse = new HotelResponse();
        hotelResponse.setName(hotel.getName());
        hotelResponse.setId(hotel.getId());
        hotelResponse.setRating(hotel.getRating());
        hotelResponse.setAddress(hotel.getAddress());
        hotelResponse.setPhoneNumber(hotel.getPhoneNumber());
        hotelResponse.setDescription(hotel.getDescription());
        hotelResponse.setImages(hotel.getImages());
        response.setHotelResponse(hotelResponse);
//        hotelResponse.setStatus(hotel.get);

        response.setId(tour.getId());

        // carCompanyResponse
        CarCompanyResponse carCompanyResponse = new CarCompanyResponse();
        carCompanyResponse.setCarCompanyName(tour.getCarCompany().getName());
        carCompanyResponse.setCarCompanyId(tour.getCarCompany().getId()
        );

        response.setCarCompanyResponse(carCompanyResponse);

        List<TourDataByDate> tourDataByDates = new ArrayList<>();
        List<TourDataSql> tourDataSqlList = tourRepository.getResourceAvailable(tour.getRoute().getId(), tour.getHotel().getId(), tour.getCarCompany().getId(), tour.getNumberPeople());
        tourDataSqlList.forEach((tourDataSql) -> {
            TourDataByDate tourDataByDate = new TourDataByDate();


            // Schedule Des
            Schedule sDes =
                    scheduleRepository.findById(tourDataSql.getSchedule1Id()).orElseThrow();
            ScheduleResponse scheduleDes = ScheduleResponse.builder()
                    .Id(sDes.getId())
                    .arrivalName(sDes.getRoute().getDestinationStation().getName())
                    .departureName(sDes.getRoute().getOriginStation().getName())
                    .arrivalTime(sDes.getArrivalTime())
                    .departureTime(sDes.getDepartureTime()).priceForOneTicket(BigDecimal.valueOf(tour.getTotalPrice()))
                    .routeId(tourDataSql.getRoute1Id())
                    .priceForOneTicket(sDes.getPrice_for_one_seat())
                    .code(sDes.getVehicleCode().getCode())
                    .build();
            tourDataByDate.setScheduleDes(scheduleDes);

            // Schedule Origin
            Schedule sOrign = scheduleRepository.findById(tourDataSql.getSchedule2Id()).orElseThrow();
            ScheduleResponse scheduleOrigin = ScheduleResponse.builder()
                    .Id(sOrign.getId())
                    .arrivalName(sOrign.getRoute().getDestinationStation().getName())
                    .departureName(sOrign.getRoute().getOriginStation().getName())
                    .arrivalTime(sOrign.getArrivalTime())
                    .departureTime(sOrign.getDepartureTime()).priceForOneTicket(BigDecimal.valueOf(tour.getTotalPrice()))
                    .routeId(tourDataSql.getRoute2Id())
                    .priceForOneTicket(sOrign.getPrice_for_one_seat())
                    .build();
            tourDataByDate.setScheduleOrigin(scheduleOrigin);

            // Schedule Seat Destination
            List<ScheduleSeatResponse> ssDes = new ArrayList<>();
            tourDataSql.getScheduleSeatIds1().forEach((id) -> {
                ScheduleSeat seatDes = scheduleSeatRepository.findById(id).orElseThrow();
                ScheduleSeatResponse ssR = new ScheduleSeatResponse();
                ssR.setSeatId(seatDes.getId());
                ssR.setSeatNumber(seatDes.getSeatNumber());
                ssDes.add(ssR);
            });
            tourDataByDate.setScheduleSeatsDes(ssDes);

            // ScheduleSeat Origin
            List<ScheduleSeatResponse> ssOrigin = new ArrayList<>();
            tourDataSql.getScheduleSeatIds2().forEach((id) -> {
                ScheduleSeat seatOrigin = scheduleSeatRepository.findById(id).orElseThrow();
                ScheduleSeatResponse ssR = new ScheduleSeatResponse();
                ssR.setSeatId(seatOrigin.getId());
                ssR.setSeatNumber(seatOrigin.getSeatNumber());

                ssOrigin.add(ssR);
            });
            tourDataByDate.setScheduleSeatsOrigin(ssOrigin);

            // Room Response
            List<RoomResponse> roomResponses = new ArrayList<>();
            tourDataSql.getHotelRoomIds().forEach((roomId) -> {
                Room room = roomRepository.findById(roomId).orElseThrow();

                RoomResponse roomResponse = new RoomResponse();
                roomResponse.setRoomName(room.getRoomName());
                roomResponse.setId(room.getId());
                roomResponse.setMaxSize(room.getMaxSize());
                roomResponse.setPrice(room.getPrice());
                roomResponse.setTypeOfRoom(room.getTypeOfRoom());
                roomResponse.setRating(room.getRating());
                roomResponse.setAvailable(true);
                roomResponse.setImages(
                        room.getImages().stream()
                                .map(roomImage -> roomImage.getImage().getUrl())
                                .collect(Collectors.toList())
                );
                roomResponse.setRoomAmenities(room.getRoomAmenities().stream().map(roomAmenitiesMapper::toResponse).toList());

                // Thời gian CheckIn phong nho? hon 1h so voi thoi` gian den
                // (Da~ Check Trong Sql) -> Nếu loi~ thi` do Sql Sai
                roomResponse.setCheckInTime(scheduleDes.getArrivalTime().minusHours(1));

                // Thời gian checkOut phong` nho? hon thoi` gian Xe khoi? hanh` 15
                roomResponse.setCheckOutTime(scheduleOrigin.getDepartureTime().minusMinutes(15));


                roomResponses.add(roomResponse);
            });
            tourDataByDate.setRooms(roomResponses);

            tourDataByDates.add(tourDataByDate);
        });
        response.setTourDataByDates(tourDataByDates);
        return response;
    }

    @Override
    public List<TourClientResponse> getAllTourClient() {
        List<Tour> tour = tourRepository.findAll();
        return tour.stream().
                map(this::convertTourToTourClientResponse).toList();
    }

    @Override
    public List<TourClientResponse> getTourByDestination(String cityDesId, String cityOriginId) {
        List<Tour> res = tourRepository.getTourByCityId(cityDesId, cityOriginId);
        return res.stream().
                map(this::convertTourToTourClientResponse).toList();
    }

    @Override
    public List<Map<String, Object>> getTourTopUsed() {
        List<Tour> tours = tourRepository.getTourTop1Used();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Tour tour : tours) {
            Map<String, Object> tourMap = new HashMap<>();
            tourMap.put("tourTitle", tour.getTitle());
            tourMap.put("tourId", tour.getId());
            tourMap.put("tourDescription", tour.getDescription());
            tourMap.put("tourRating", tour.getRating());
            tourMap.put("tourTags", tour.getTags().stream().map(Tag::getName).toList());
            tourMap.put("tourImage", tour.getImages().getFirst().getUrl());
            tourMap.put("tourDays", tour.getDay() + "N/" + tour.getNight() + "Đ");
            tourMap.put("tourDestination", tour.getRoute().getDestinationStation().getCity().getNameCity());
            tourMap.put("tourPeople", tour.getNumberPeople());
            tourMap.put("tourUsed", tour.getUserUsed().toArray().length);
            response.add(tourMap);
        }
        return response;
    }

    @Override
    public List<Map<String, Object>> getTourHasDestination(String cityDesId) {
        List<Tour> res = tourRepository.getTourHasCityDestination(cityDesId);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Tour tour : res) {
            Map<String, Object> tourMap = new HashMap<>();
            tourMap.put("tourTitle", tour.getTitle());
            tourMap.put("tourId", tour.getId());
            tourMap.put("tourDescription", tour.getDescription());
            tourMap.put("tourRating", tour.getRating());
            tourMap.put("tourTags", tour.getTags().stream().map(Tag::getName).toList());
            tourMap.put("tourImage", tour.getImages().isEmpty() ? null : tour.getImages().getFirst().getUrl());
            tourMap.put("tourDays", tour.getDay() + "N/" + tour.getNight() + "Đ");
            tourMap.put("tourDestination", tour.getRoute().getDestinationStation().getCity().getNameCity());
            tourMap.put("tourPeople", tour.getNumberPeople());
            tourMap.put("tourUsed", tour.getUserUsed().toArray().length);
            response.add(tourMap);
        }
        return response;
    }

    @Override
    public List<Map<String, Object>> getTourHasCheckIn(Integer checkInId) {
        List<Tour> res = tourRepository.getTourHasCheckIn(checkInId);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Tour tour : res) {
            Map<String, Object> tourMap = new HashMap<>();
            tourMap.put("tourTitle", tour.getTitle());
            tourMap.put("tourId", tour.getId());
            tourMap.put("tourDescription", tour.getDescription());
            tourMap.put("tourRating", tour.getRating());
            tourMap.put("tourTags", tour.getTags().stream().map(Tag::getName).toList());
            tourMap.put("tourImage", tour.getImages().getFirst().getUrl());
            tourMap.put("tourDays", tour.getDay() + "N/" + tour.getNight() + "Đ");
            tourMap.put("tourDestination", tour.getRoute().getDestinationStation().getCity().getNameCity());
            tourMap.put("tourPeople", tour.getNumberPeople());
            tourMap.put("tourUsed", tour.getUserUsed().toArray().length);
            response.add(tourMap);
        }
        return response;
    }

    @Override
    public List<Map<String, Object>> getTourHaveCityIn(String areaId) {
        if (areaId == null) {
            throw new AppException(ErrorType.inputFieldInvalid);
        }
        if (areaRepository.findById(areaId).isEmpty()) {
            throw new AppException(ErrorType.notFound);
        }
        List<Tour> tours = tourRepository.getTourHaveCityIn(areaId);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Tour tour : tours) {
            Map<String, Object> tourMap = new HashMap<>();
            tourMap.put("tourTitle", tour.getTitle());
            tourMap.put("tourId", tour.getId());
            tourMap.put("tourDescription", tour.getDescription());
            tourMap.put("tourRating", tour.getRating());
            tourMap.put("tourTags", tour.getTags().stream().map(Tag::getName).toList());
            tourMap.put("tourImage", tour.getImages().isEmpty() ? null : tour.getImages().getFirst().getUrl());
            tourMap.put("tourDays", tour.getDay() + "N/" + tour.getNight() + "Đ");
            tourMap.put("tourDestination", tour.getRoute().getDestinationStation().getCity().getNameCity());
            tourMap.put("tourPeople", tour.getNumberPeople());
            tourMap.put("tourUsed", tour.getUserUsed().toArray().length);
            response.add(tourMap);
        }

        return response;
    }


    public TourClientResponse convertTourToTourClientResponse(Tour tour) {
        TourClientResponse clientResponse = new TourClientResponse();
        clientResponse.setTourDes(tour.getDescription());
        clientResponse.setTourTitle(tour.getTitle());
        clientResponse.setTourId(tour.getId());
        clientResponse.setAdmin(tour.getAdmin());
        clientResponse.getAdmin().setPassword("Cung hong coa dau");
        clientResponse.getAdmin().setPassword("Hong be oi/.");
        clientResponse.setTimeCreate(tour.getCreateAt());
        clientResponse.setTimeUpdate(tour.getUpdateAt());
        clientResponse.setTags(tour.getTags());
        clientResponse.setListUserUsed(
                tour.getUserUsed().stream()
                        .map(user -> user.getId())  // Lấy ID của mỗi user
                        .collect(Collectors.toList())  // Thu thập kết quả vào một danh sách
        );
        if (!tour.getImages().isEmpty()) {
            clientResponse.setUrlImage(tour.getImages().getFirst().getUrl());
        }
        return clientResponse;
    }


}
