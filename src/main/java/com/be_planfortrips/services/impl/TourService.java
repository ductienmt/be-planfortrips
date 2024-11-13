package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.TourDTO;
import com.be_planfortrips.dto.response.TourResponse;
import com.be_planfortrips.entity.Tag;
import com.be_planfortrips.entity.Tour;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.TourMapper;
import com.be_planfortrips.repositories.TagRepository;
import com.be_planfortrips.repositories.TourRepository;
import com.be_planfortrips.services.interfaces.ITourService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TourService implements ITourService {
    TourRepository tourRepository;
    TagRepository tagRepository;
    TourMapper tourMapper;
    @Override
    public TourResponse createTour(TourDTO TourDTO) throws Exception {
        Tour tour = tourMapper.toEntity(TourDTO);
        List<Tag> tags = TourDTO.getTagNames()!= null
                ? tagRepository.findAllByNameIn(TourDTO.getTagNames())
                :new ArrayList<>();
        tour.setTags(tags);
        tourRepository.save(tour);
        return tourMapper.toResponse(tour);
    }

    @Override
    public TourResponse updateTour(Integer id, TourDTO TourDTO) throws Exception {
        Tour tourExisting = tourRepository.findById(id)
                .orElseThrow(()-> {throw new AppException(ErrorType.notFound);});
        List<Tag> tags = TourDTO.getTagNames()!= null
                ? tagRepository.findAllByNameIn(TourDTO.getTagNames())
                :new ArrayList<>();
        tourExisting.setTags(tags);
        tourExisting.setId(id);
        tourRepository.save(tourExisting);
        return tourMapper.toResponse(tourExisting);
    }

    @Override
    public Page<TourResponse> getTours(PageRequest request,String title,Integer rating,List<String> tags) {
        if(title==null && tags.size() == 0 && rating == null){
            return tourRepository.findAll(request).map(tourMapper::toResponse);
        }else{
            System.out.println(tourRepository.searchTours(request,title,rating,tags));
            return tourRepository.searchTours(request,title,rating,tags).map(tourMapper::toResponse);
        }
    }

    @Override
    public TourResponse getByTourId(Integer id) throws Exception {
        Tour tourExisting = tourRepository.findById(id).orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        return tourMapper.toResponse(tourExisting);
    }

    @Override
    public void deleteTourById(Integer id) {
        Optional<Tour> tour = tourRepository.findById(id);
        tour.ifPresent(tourRepository::delete);
    }
}
