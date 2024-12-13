package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.VehicleDTO;
import com.be_planfortrips.dto.response.VehicleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IVehicleService {
    VehicleResponse createVehicle(VehicleDTO VehicleDto) throws Exception;
    VehicleResponse updateVehicle(String code,VehicleDTO VehicleDto) throws Exception;
    Page<VehicleResponse> getVehicles(PageRequest request,String keyword);
    VehicleResponse getByVehicleId(String code) throws Exception;
    void deleteVehicleById(String code) throws Exception;
    List<VehicleResponse> getVehiclesByEnterpriseId();
}
