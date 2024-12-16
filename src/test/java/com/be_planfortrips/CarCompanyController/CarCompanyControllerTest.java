package com.be_planfortrips.CarCompanyController;

import com.be_planfortrips.controllers.CarCompanyController;
import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.response.CarResponse;

import com.be_planfortrips.services.interfaces.ICarCompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CarCompanyControllerTest {

    @InjectMocks
    private CarCompanyController carCompanyController;

    @Mock
    private ICarCompanyService iCarService;

    @Mock
    private BindingResult bindingResult;

    private CarCompanyDTO carCompanyDTO;
    private CarResponse carResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        carCompanyDTO = new CarCompanyDTO();
        carResponse = new CarResponse();
    }

    @Test
    void createCarCompany() throws Exception{
        when(bindingResult.hasErrors()).thenReturn(false);
        when(iCarService.createCar(carCompanyDTO)).thenReturn(carResponse);

        ResponseEntity<?> response = carCompanyController.createCarCompany(carCompanyDTO, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carResponse, response.getBody());
    }

    @Test
    void createCarCompany1() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(
                new FieldError("carCompanyDTO", "name", "Car company name is required")));

        ResponseEntity<?> response = carCompanyController.createCarCompany(carCompanyDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        List<String> errors = (List<String>) response.getBody();
        assertEquals(1, errors.size());
        assertEquals("Car company name is required", errors.get(0));
    }

    @Test
    void updateCarCompany() throws Exception {
        int carCompanyId = 1;
        when(bindingResult.hasErrors()).thenReturn(false);
        when(iCarService.updateCar(carCompanyId, carCompanyDTO)).thenReturn(carResponse);

        ResponseEntity<?> response = carCompanyController.updateCarCompany(carCompanyId, carCompanyDTO, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carResponse, response.getBody());
    }



    @Test
    void deleteCarCompanyById() {
        int carCompanyId = 1;
        doNothing().when(iCarService).deleteCarById(carCompanyId);

        ResponseEntity<?> response = carCompanyController.deleteCarCompanyById(carCompanyId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(iCarService, times(1)).deleteCarById(carCompanyId);
    }

    @Test
    void getCarCompanyById() throws Exception{
        int carCompanyId = 1;
        when(iCarService.getByCarId(carCompanyId)).thenReturn(carResponse);

        ResponseEntity<?> response = carCompanyController.getCarCompanyById(carCompanyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carResponse, response.getBody());
    }

    @Test
    void uploadImages() throws Exception{
        int carCompanyId = 1;
        List<MultipartFile> files = Collections.emptyList();
        when(iCarService.uploadImage(carCompanyId, files)).thenReturn(carResponse);

        ResponseEntity<?> response = carCompanyController.uploadImages(carCompanyId, files);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carResponse, response.getBody());
    }

    @Test
    void deleteImages() throws Exception{
        int carCompanyId = 1;
        List<Integer> imageIdsList = List.of(1, 2, 3);
        when(iCarService.deleteImage(carCompanyId, imageIdsList)).thenReturn(carResponse);

        ResponseEntity<?> response = carCompanyController.deleteImages(carCompanyId, "1,2,3");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carResponse, response.getBody());
    }
}