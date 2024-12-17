package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.VehicleDTO;
import com.be_planfortrips.dto.response.VehicleResponse;
import com.be_planfortrips.entity.CarCompany;
import com.be_planfortrips.entity.TypeVehicle;
import com.be_planfortrips.entity.Vehicle;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.mappers.impl.VehicleMapper;
import com.be_planfortrips.repositories.CarCompanyRepository;
import com.be_planfortrips.repositories.ScheduleRepository;
import com.be_planfortrips.repositories.TicketRepository;
import com.be_planfortrips.repositories.VehicleRepository;
import com.be_planfortrips.services.interfaces.IVehicleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VehicleService implements IVehicleService {
    VehicleRepository vehicleRepository;
    CarCompanyRepository carCompanyRepository;
    VehicleMapper vehicleMapper;
    private final TokenMapperImpl tokenMapperImpl;
    private final TicketRepository ticketRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public VehicleResponse createVehicle(VehicleDTO vehicleDto) throws Exception {
        CarCompany existingCarCompany = carCompanyRepository.findById(vehicleDto.getCarCompanyId())
                .orElseThrow(
                        () -> new Exception("Not found")
                );
        Vehicle existingVehicle = vehicleRepository.findByCode(vehicleDto.getCode());
        if (existingVehicle != null) {
            throw new Exception("Code is exist");
        }
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        vehicle.setCarCompany(existingCarCompany);
        vehicleRepository.saveAndFlush(vehicle);
        return vehicleMapper.toResponse(vehicle);
    }

    @Override
    @Transactional
    public VehicleResponse updateVehicle(String code, VehicleDTO vehicleDto) throws Exception {
        CarCompany existingCarCompany = carCompanyRepository.findById(vehicleDto.getCarCompanyId())
                .orElseThrow(
                        () -> new Exception("Not found")
                );
        Vehicle existingVehicle = vehicleRepository.findByCode(code);
        if (existingVehicle == null) {
            throw new Exception("Not found");
        }
        existingVehicle = vehicleMapper.toEntity(vehicleDto);
        existingVehicle.setCarCompany(existingCarCompany);
        vehicleRepository.saveAndFlush(existingVehicle);
        return vehicleMapper.toResponse(existingVehicle);
    }

    @Override
    public Page<VehicleResponse> getVehicles(PageRequest request, String keyword) {
        return vehicleRepository.searchVehicles(request, keyword).map(vehicleMapper::toResponse);
    }

    @Override
    public VehicleResponse getByVehicleId(String code) throws Exception {
        Vehicle existingVehicle = vehicleRepository.findByCode(code);
        if (existingVehicle == null) {
            throw new Exception("Not found");
        }
        return vehicleMapper.toResponse(existingVehicle);
    }

    @Override
    @Transactional
    public void deleteVehicleById(String code) throws Exception {
        Vehicle optionalVehicle = vehicleRepository.findByCode(code);
        System.out.println(optionalVehicle);
        if (optionalVehicle == null) {
            throw new AppException(ErrorType.notFound);
        }
        long ticketCount = ticketRepository.countTicketsByVehicleCode(code);
        if (ticketCount > 0 || scheduleRepository.countSchedulesByVehicleCode(code) > 0) {
            throw new RuntimeException("Xe đã có vé, không thể xóa");
        }


        vehicleRepository.delete(optionalVehicle);
    }

    @Override
    public List<VehicleResponse> getVehiclesByEnterpriseId(String filter) {
        List<Vehicle> vehicles;

        if (filter.equals("all")) {
            vehicles = this.vehicleRepository.getVehicleByEnterpriseId(tokenMapperImpl.getIdEnterpriseByToken());
        } else if (filter.equals("running")) {
            vehicles = this.vehicleRepository.getTheRunningVehicle(tokenMapperImpl.getIdEnterpriseByToken());
        } else if (filter.equals("notInUse")) {
            vehicles = this.vehicleRepository.getNotInUseVehicle(tokenMapperImpl.getIdEnterpriseByToken());
        } else if (filter.equals("standard")) {
            vehicles = this.vehicleRepository.getVehicleByTypeAndEnterpriseId(TypeVehicle.Standard, tokenMapperImpl.getIdEnterpriseByToken());
        } else if (filter.equals("vip")) {
            vehicles = this.vehicleRepository.getVehicleByTypeAndEnterpriseId(TypeVehicle.Vip, tokenMapperImpl.getIdEnterpriseByToken());
        } else {
            throw new AppException(ErrorType.inputFieldInvalid);
        }

        vehicles.sort(Comparator.comparing(Vehicle::getCode));

        return vehicles.stream().map(this.vehicleMapper::toResponse).toList();
    }

    @Override
    public List<VehicleResponse> searchVehicle(String keyword) {
        return this.vehicleRepository.getVehicleByEntepriseIdAndWithKeyword(keyword, tokenMapperImpl.getIdEnterpriseByToken()).stream().map(this.vehicleMapper::toResponse).toList();
    }


}
