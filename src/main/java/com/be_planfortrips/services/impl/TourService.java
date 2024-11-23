package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.TourDTO;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.dto.response.rsTourResponse.TourScheduleResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    ImageRepository imageRepository;
    TourMapper tourMapper;
    CloudinaryService cloudinaryService;
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
        CarCompany carExisting = carCompanyRepository
                .findById(TourDTO.getCarCompanyId())
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
    public List<TourScheduleResponse> getScheduleAvailable(LocalDateTime day, String cityId) {
        return List.of();
    }
}
