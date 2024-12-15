package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.HotelDto;
import com.be_planfortrips.dto.response.BookingCustomer;
import com.be_planfortrips.dto.response.HotelResponses.AvailableHotels;
import com.be_planfortrips.dto.response.RoomResponse;
import com.be_planfortrips.entity.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.HotelMapper;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.HotelRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.dto.response.HotelResponse;
import com.be_planfortrips.repositories.RouteRepository;
import com.be_planfortrips.services.interfaces.IHotelService;
import com.be_planfortrips.utils.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelService implements IHotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelService.class);
    HotelRepository hotelRepository;

    AccountEnterpriseRepository enterpriseRepository;

    ImageRepository imageRepository;

    HotelMapper hotelMapper;

    RoomServiceImpl roomServiceImpl;

    CloudinaryService cloudinaryService;
    RouteRepository routeRepository;
    private final TokenMapperImpl tokenMapperImpl;
    private final BookingHotelServiceImpl bookingHotelServiceImpl;

    @Override
    @Transactional
    public HotelResponse createHotel(HotelDto hotelDto) throws Exception {
        AccountEnterprise accountEnterprise = enterpriseRepository.findById(tokenMapperImpl.getIdEnterpriseByToken())
                .orElseThrow(() -> new Exception("Not found"));
        Hotel hotel = hotelMapper.toEntity(hotelDto);
        if (!Utils.isValidPhoneNumber(hotel.getPhoneNumber())) throw new AppException(ErrorType.phoneNotValid);
        hotel.setAccountEnterprise(accountEnterprise);
        hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public HotelResponse updateHotel(Long id, HotelDto hotelDto) throws Exception {
        Hotel existHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new Exception("Not found"));
        AccountEnterprise accountEnterprise = enterpriseRepository.findById(tokenMapperImpl.getIdEnterpriseByToken())
                .orElseThrow(() -> new Exception("Not found"));
        hotelMapper.updateEntityFromDto(hotelDto, existHotel);
        if (!Utils.isValidPhoneNumber(existHotel.getPhoneNumber())) throw new AppException(ErrorType.notFound);
        existHotel.setId(id);
        existHotel.setAccountEnterprise(accountEnterprise);
        hotelRepository.saveAndFlush(existHotel);
        return hotelMapper.toResponse(existHotel);
    }

    @Override
    public Page<HotelResponse> searchHotels(PageRequest request, String keyword, Integer rating) {
        if (rating != null) {
            if (rating < 0 || rating > 5) throw new AppException(ErrorType.ratingInvalid);
        }
        return hotelRepository.searchHotels(request, keyword, rating).map(hotel -> hotelMapper.toResponse(hotel));
    }


    @Override
    public HotelResponse getByHotelId(Long id) throws Exception {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new Exception("Not found"));
        return hotelMapper.toResponse(hotel);
    }

    @Override
    public HotelResponse getHotelByRoomId(Long id) throws Exception {
        RoomResponse room = roomServiceImpl.getRoomById(id);
        if(room == null){
            throw new AppException(ErrorType.notFound);
        }
        Hotel hotel = hotelRepository.getHotelByRoomId(room.getId());
        return hotelMapper.toResponse(hotel);
    }


    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Optional<Hotel> orderOptional = hotelRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new AppException(ErrorType.notFound);
        }
        List<BookingCustomer> bookingCustomers = bookingHotelServiceImpl.findCustomersByEnterpriseId("all");
        if (bookingCustomers.isEmpty()){
            orderOptional.ifPresent(hotelRepository::delete);
        }
        Hotel hotel = orderOptional.get();
        hotel.setStatus(false);
        hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public HotelResponse createHotelImage(Long hotelId, List<MultipartFile> files) throws Exception {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new Exception("Hotel not found"));

        if (files == null || files.isEmpty()) {
            throw new Exception("No files to upload");
        }

        List<Image> imageList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new Exception("One or more files are empty");
            }
            if (file.getSize() > 10 * 1024 * 1024) { // Giới hạn 10MB
                throw new Exception("File " + file.getOriginalFilename() + " is too large! Maximum size is 10MB");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new Exception("File " + file.getOriginalFilename() + " must be an image");
            }

            Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, "hotels");
            String imageUrl = (String) uploadResult.get("secure_url");
            Image image = new Image();
            image.setUrl(imageUrl);
            image = imageRepository.saveAndFlush(image);
            imageList.add(image);
        }
        hotel.setImages(imageList);
        hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public HotelResponse deleteImage(Long id, List<Integer> imageIds) throws Exception {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorType.notFound));
        List<Image> images = hotel.getImages();
        System.out.println("Total images for hotel ID " + hotel.getId() + ": " + images.size());

        List<Image> imagesToDelete = images.stream()
                .filter(image -> imageIds.contains(Integer.valueOf(String.valueOf(image.getId()))))
                .collect(Collectors.toList());
        if (imagesToDelete.isEmpty()) {
            System.out.println("No images found to delete.");
        } else {
            System.out.println("Images to delete: " + imagesToDelete.size());
        }
        if (imagesToDelete.isEmpty()) {
            throw new Exception("No images found to delete");
        }

        for (Image image : imagesToDelete) {
            try {
                String publicId = cloudinaryService.getPublicIdFromUrl(image.getUrl());
                cloudinaryService.deleteFile(publicId);
                hotel.getImages().remove(image);
                imageRepository.delete(image);
            } catch (Exception e) {
                throw new Exception("Error deleting image: " + e.getMessage());
            }
        }
        hotelRepository.saveAndFlush(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    public Map<String, Object> getRoomAvailable(LocalDateTime checkIn, LocalDateTime checkOut, String destination) {
        List<RoomResponse> availableRooms = roomServiceImpl.getRoomAvailable(checkIn, checkOut, destination);
        System.out.println("Available rooms: " + availableRooms.size());
        Map<Long, HotelResponse> hotelResponseMap = new HashMap<>();

        for (RoomResponse roomResponse : availableRooms) {
            Long hotelId = roomResponse.getHotel().getId();
            if (!hotelResponseMap.containsKey(hotelId)) {
                try {
                    hotelResponseMap.put(hotelId, this.getByHotelId(hotelId));
                } catch (Exception e) {
                    throw new AppException(ErrorType.notFound);
                }
            }
        }

        Map<String, Object> hotelMap = new HashMap<>();

        for (RoomResponse roomResponse : availableRooms) {
            HotelResponse hotelResponse = hotelResponseMap.get(roomResponse.getHotel().getId());

            if (!hotelMap.containsKey(hotelResponse.getId().toString())) {
                Map<String, Object> hotelInfo = new HashMap<>();
                hotelInfo.put("hotelId", hotelResponse.getId());
                hotelInfo.put("hotelName", hotelResponse.getName());
                hotelInfo.put("hotelAddress", hotelResponse.getAddress());
                hotelInfo.put("hotelPhonenumber", hotelResponse.getPhoneNumber());
                hotelInfo.put("rating", hotelResponse.getRating());
                hotelInfo.put("hotelImages", hotelResponse.getImages());
                hotelInfo.put("roomAvailable", new ArrayList<Map<String, Object>>());
                hotelMap.put(hotelResponse.getId().toString(), hotelInfo);
            }

            Map<String, Object> roomInfo = new HashMap<>();
            roomInfo.put("roomId", roomResponse.getId());
            roomInfo.put("roomName", roomResponse.getRoomName());
            roomInfo.put("roomType", roomResponse.getTypeOfRoom());
            roomInfo.put("price", roomResponse.getPrice());
            roomInfo.put("maxPeople", roomResponse.getMaxSize());
            roomInfo.put("availability", roomResponse.isAvailable());

            Map<String, Object> hotelInfo = (Map<String, Object>) hotelMap.get(hotelResponse.getId().toString());
            List<Map<String, Object>> roomResponses = (List<Map<String, Object>>) hotelInfo.get("roomAvailable");
            roomResponses.add(roomInfo);
        }

        return hotelMap;
    }

    @Override
    public List<Map<String, Object>> getHotelDetail() {
        List<Hotel> hotels = this.hotelRepository.findByEnterpriseId(tokenMapperImpl.getIdEnterpriseByToken());
        List<Map<String, Object>> listHotelResponse = new ArrayList<>();
        for (Hotel hotel : hotels) {
            Map<String, Object> hotelResponse = new HashMap<>();
            hotelResponse.put("hotelId", hotel.getId());
            hotelResponse.put("hotelName", hotel.getName());
            hotelResponse.put("hotelAddress", hotel.getAddress());
            hotelResponse.put("hotelPhoneNumber", hotel.getPhoneNumber());
            hotelResponse.put("rating", hotel.getRating());
            hotelResponse.put("hotelImages", hotel.getImages());
            hotelResponse.put("hotelDescription", hotel.getDescription());
            hotelResponse.put("hotelStatus", hotel.getStatus());
            listHotelResponse.add(hotelResponse);
        }
        return listHotelResponse;
    }

    @Override
    public List<HotelResponse> getByEnterpriseId(Long enterpriseId) {
        List<Hotel> hotels = this.hotelRepository.findByEnterpriseId(enterpriseId);
        return List.of();
    }

    @Override
    public List<HotelResponse> getByRouteId(String routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> {
                    throw new AppException(ErrorType.notFound);
                });
        return hotelRepository.findHotelByRouteId(route.getId()).stream().map(hotelMapper::toResponse).toList();
    }
    public class StringUtils {
        public static String removeAccents(String input) {
            if (input == null) return null;
            String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase();
            return normalized;
        }
    }
    @Override
    public Page<HotelResponse> findHotelAvailable(PageRequest request, String keyword, LocalDateTime dateTime, Long days) {
        String searchKeyword = (keyword == null || keyword.trim().isEmpty()) ?
                null : StringUtils.removeAccents(keyword);

        Page<HotelResponse> hotelResponses = hotelRepository.findAvailableHotels(
                request,
                dateTime,
                dateTime.plusDays(days),
                searchKeyword
        ).map(hotel -> hotelMapper.toResponse(hotel));

        return hotelResponses;
    }

    @Override
    public List<Map<String, Object>> getHotelsSamePrice(double price, String destination) {
        BigDecimal minPrice = BigDecimal.valueOf(price - 50);
        BigDecimal maxPrice = BigDecimal.valueOf(price + 50);

        List<Object[]> hotels = hotelRepository.findHotelsWithRoomsInPriceRange(minPrice, maxPrice, destination);
        if (hotels.isEmpty()) {
            minPrice = BigDecimal.valueOf(price - 100);
            maxPrice = BigDecimal.valueOf(price + 100);
            hotels = hotelRepository.findHotelsWithRoomsInPriceRange(minPrice, maxPrice, destination);
        }
        System.out.println("Hotels found: " + hotels.size());

        Map<Long, Map<String, Object>> hotelMap = new HashMap<>();
        for (Object[] result : hotels) {
            Hotel hotel = (Hotel) result[0];
            Room room = (Room) result[1];

            hotelMap.computeIfAbsent(hotel.getId(), k -> {
                Map<String, Object> map = new HashMap<>();
                map.put("hotelId", hotel.getId());
                map.put("hotelName", hotel.getName());
                map.put("hotelAddress", hotel.getAddress());
                map.put("hotelImage", hotel.getImages().stream().limit(1).collect(Collectors.toList()));
                map.put("hotelAmenities", hotel.getHotelAmenities().stream().limit(3).collect(Collectors.toList()));
                map.put("rating", hotel.getRating());
                map.put("roomPrice", room.getPrice());
                return map;
            });
        }

        return new ArrayList<>(hotelMap.values());
    }

    @Override
    public void changeStatus(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorType.notFound));
        hotel.setStatus(!hotel.getStatus());
        hotelRepository.save(hotel);
    }

    @Override
    public Page<Map<String, Object>> searchEnterprise(String keyword, Pageable pageable) {
        Long enterpriseId = tokenMapperImpl.getIdEnterpriseByToken();

        Page<Hotel> hotels = hotelRepository.searchEnterprise(keyword, enterpriseId, pageable);

        List<Map<String, Object>> listHotelResponse = new ArrayList<>();
        for (Hotel hotel : hotels) {
            Map<String, Object> hotelResponse = new HashMap<>();
            hotelResponse.put("hotelId", hotel.getId());
            hotelResponse.put("hotelName", hotel.getName());
            hotelResponse.put("hotelAddress", hotel.getAddress());
            hotelResponse.put("hotelPhoneNumber", hotel.getPhoneNumber());
            hotelResponse.put("rating", hotel.getRating());
            hotelResponse.put("hotelImages", hotel.getImages());
            hotelResponse.put("hotelDescription", hotel.getDescription());
            hotelResponse.put("hotelStatus", hotel.getStatus());
            listHotelResponse.add(hotelResponse);
        }

        return new PageImpl<>(listHotelResponse, pageable, hotels.getTotalElements());
    }

}