package com.be_planfortrips.VehicleController;

import com.be_planfortrips.controllers.VehicleController;
import com.be_planfortrips.dto.VehicleDTO;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.dto.response.VehicleResponse;
import com.be_planfortrips.services.interfaces.IVehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VehicleControllerTest {

    @Mock
    private IVehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private VehicleDTO vehicleDTO;
    private VehicleResponse vehicleResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicleResponse = new VehicleResponse("V002", "37A-123.45", 5, "John Doe", "123456789", null, "Car");
    }

    @Test
    void testCreateVehicle_Success() throws Exception {
        when(vehicleService.createVehicle(any(VehicleDTO.class))).thenReturn(vehicleResponse);

        ResponseEntity<?> response = vehicleController.createVehicle(vehicleDTO, mock(BindingResult.class));
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(vehicleResponse, response.getBody());
    }


    @Test
    void testGetVehicleById_ValidId() throws Exception {
        when(vehicleService.getByVehicleId("V002+++")).thenReturn(vehicleResponse);
        ResponseEntity<?> response = vehicleController.getCarCompanyById("V002");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(vehicleResponse, response.getBody());
    }


    @Test
    void testCreateVehicle_BindingErrors() {

        BindingResult result = mock(BindingResult.class);


        when(result.hasErrors()).thenReturn(true);
        when(result.getFieldErrors()).thenReturn(Arrays.asList(
                new FieldError("vehicleDTO", "code", "Mã phương tiện là bắt buộc"),
                new FieldError("vehicleDTO", "plateNumber", "Biển số xe là bắt buộc")
        ));


        ResponseEntity<?> response = vehicleController.createVehicle(vehicleDTO, result);


        assertEquals(400, response.getStatusCodeValue());


        List<String> errorMessages = (List<String>) response.getBody();

        assertTrue(errorMessages.contains("Mã phương tiện là bắt buộc"));
        assertTrue(errorMessages.contains("Biển số xe là bắt buộc"));


        System.out.println("Lỗi: " + errorMessages);
    }


    @Test
    void testUpdateVehicle_Success() throws Exception {
        when(vehicleService.updateVehicle(any(String.class), any(VehicleDTO.class))).thenReturn(vehicleResponse);

        ResponseEntity<?> response = vehicleController.updateVehicle("V001", vehicleDTO, mock(BindingResult.class));
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(vehicleResponse, response.getBody());
    }

    @Test
    void testGetAllVehicles() {
        Page<VehicleResponse> vehiclePage = new PageImpl<>(List.of(vehicleResponse));
        when(vehicleService.getVehicles(any(PageRequest.class), any(String.class))).thenReturn(vehiclePage);

        ResponseEntity<?> response = vehicleController.getCarCompanies(0, 10, "");
        assertEquals(200, response.getStatusCodeValue());

        TListResponse<VehicleResponse> listResponse = (TListResponse<VehicleResponse>) response.getBody();
        assertEquals(1, listResponse.getListResponse().size());
        assertEquals(1, listResponse.getTotalPage());
    }

    @Test
    void testDeleteVehicle_Success() throws Exception {
        doNothing().when(vehicleService).deleteVehicleById("V001");

        ResponseEntity<?> response = vehicleController.deleteCarCompanyById("V001");
        assertEquals(204, response.getStatusCodeValue());
    }


    @Test
    void testGetVehicleById_Success() throws Exception {
        // Chuẩn bị: Giả lập phản hồi của dịch vụ cho một xe tồn tại
        when(vehicleService.getByVehicleId("V001")).thenReturn(vehicleResponse);

        // Hành động: Gọi phương thức của controller
        ResponseEntity<?> response = vehicleController.getCarCompanyById("V001");

        // Khẳng định: Kiểm tra rằng phản hồi là thành công
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(vehicleResponse, response.getBody());
    }

    @Test
    void testGetVehicleById_NotFound() throws Exception {
        // Chuẩn bị: Giả lập dịch vụ ném ra một ngoại lệ cho ID xe không tồn tại
        when(vehicleService.getByVehicleId("V003")).thenThrow(new RuntimeException("Xe không tồn tại"));

        // Hành động: Gọi phương thức của controller với ID không tồn tại
        ResponseEntity<?> response = vehicleController.getCarCompanyById("V003");

        // Khẳng định: Kiểm tra rằng phản hồi chỉ ra lỗi
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Xe không tồn tại", response.getBody());
    }
}