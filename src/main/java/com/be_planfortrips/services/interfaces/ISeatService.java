package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.SeatDTO;
import com.be_planfortrips.dto.response.SeatResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ISeatService {
    SeatResponse createSeat(SeatDTO seatDTO) throws Exception;
    SeatResponse updateSeat(Integer id,SeatDTO seatDTO) throws Exception;
    Page<SeatResponse> getSeats(PageRequest request);
    SeatResponse getBySeatId(Integer id) throws Exception;
    void deleteSeatById(Integer id);
//    List<SeatResponse> getAllSeatsEmpty(String vehicleCode);
    List<SeatResponse> getEmptySeatsByScheduleId(Integer scheduleId);
}
