//package com.be_planfortrips.services.impl;
//import com.be_planfortrips.dto.AirlineDto;
//import com.be_planfortrips.dto.HotelDto;
//import com.be_planfortrips.dto.HotelImageDto;
//import com.be_planfortrips.entity.*;
//import com.be_planfortrips.mappers.impl.AirlineMapper;
//import com.be_planfortrips.mappers.impl.HotelImageMapper;
//import com.be_planfortrips.mappers.impl.HotelMapper;
//import com.be_planfortrips.repositories.*;
//import com.be_planfortrips.responses.AirlineResponse;
//import com.be_planfortrips.responses.HotelImageResponse;
//import com.be_planfortrips.responses.HotelResponse;
//import com.be_planfortrips.services.interfaces.IAirlineService;
//import com.be_planfortrips.services.interfaces.IHotelService;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.TypeMap;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
//public class AirlineService implements IAirlineService {
//    AirlineRepository airlineRepository;
//    EnterpriseRepository enterpriseRepository;
//    AirlineMapper airlineMapper = new AirlineMapper();
//    @Override
//    @Transactional
//    public Airline saveAirline(AirlineDto airlineDto) throws Exception {
//        AccountEnterprise accountEnterprise = enterpriseRepository.findById(airlineDto.getEnterpriseId())
//                .orElseThrow(()->new Exception("Enterprise is not found"));
//        Airline airline = airlineMapper.toEntity(airlineDto);
//        airline.setAccountEnterprise(accountEnterprise);
//        return airlineRepository.saveAndFlush(airline);
//    }
//
//    @Override
//    public Page<AirlineResponse> getAirlines(PageRequest request) {
//        System.out.println(airlineRepository.findAll(request));
//        return airlineRepository.findAll(request).map(hotel -> airlineMapper.toResponse(hotel));
//    }
//
//    @Override
//    @Transactional
//    public Airline updateAirline(Long id, AirlineDto airlineDto) throws Exception {
//        Airline existingAirline = airlineRepository.findById(id)
//                .orElseThrow(()->new Exception("Airplane is not found"));
//        AccountEnterprise accountEnterprise = enterpriseRepository.findById(airlineDto.getEnterpriseId())
//                .orElseThrow(()->new Exception("Enterprise is not found"));
//        airlineMapper.updateEntityFromDto(airlineDto,existingAirline);
//        existingAirline.setId(id);
//        existingAirline.setAccountEnterprise(accountEnterprise);
//        return airlineRepository.saveAndFlush(existingAirline);
//    }
//
//    @Override
//    public AirlineResponse getAirlineById(Long id) throws Exception {
//        Airline existingAirline = airlineRepository.findById(id)
//                .orElseThrow(()->new Exception("Airplane is not found"));
//        return airlineMapper.toResponse(existingAirline);
//    }
//
//    @Override
//    @Transactional
//    public void deleteAirlineById(Long id) {
//        Optional<Airline> orderOptional = airlineRepository.findById(id);
//        orderOptional.ifPresent(airlineRepository::delete);
//    }
//}
