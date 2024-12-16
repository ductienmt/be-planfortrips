package com.be_planfortrips.BannerController;

import com.be_planfortrips.controllers.BannerController;
import com.be_planfortrips.dto.BannerDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.BannerResponseAdmin;
import com.be_planfortrips.dto.response.BannerResponseUser;
import com.be_planfortrips.services.interfaces.IBannerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BannerControllerTest {

    @InjectMocks
    private BannerController bannerController;

    @Mock
    private IBannerService bannerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBannersUser() {
        List<BannerResponseUser> mockBanners = new ArrayList<>();
        when(bannerService.getAllBannersUser()).thenReturn(mockBanners);

        ResponseEntity<?> response = bannerController.getAllBannersUser();

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<List<BannerResponseUser>> apiResponse = (ApiResponse<List<BannerResponseUser>>) response.getBody();
        assertEquals(mockBanners, apiResponse.getData());
    }

    @Test
    void getAllBanners() {
        List<BannerResponseAdmin> mockBanners = new ArrayList<>();
        when(bannerService.getAllBanners()).thenReturn(mockBanners);

        ResponseEntity<?> response = bannerController.getAllBanners();

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<List<BannerResponseAdmin>> apiResponse = (ApiResponse<List<BannerResponseAdmin>>) response.getBody();
        assertEquals(mockBanners, apiResponse.getData());
    }

    @Test
    void createBanner() {
        BannerDto bannerDto = new BannerDto();
        BannerResponseAdmin mockResponse = new BannerResponseAdmin();
        when(bannerService.createBanner(bannerDto)).thenReturn(mockResponse);

        ResponseEntity<?> response = bannerController.createBanner(bannerDto);

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<BannerResponseAdmin> apiResponse = (ApiResponse<BannerResponseAdmin>) response.getBody();
        assertEquals(mockResponse, apiResponse.getData());
        assertEquals("Tạo banner thành công.", apiResponse.getMessage());
    }


    @Test
    void updateBanner1() {
        Long bannerId = 1L;
        BannerDto bannerDto = new BannerDto();
        BannerResponseAdmin mockResponse = new BannerResponseAdmin();
        when(bannerService.updateBanner(bannerId, bannerDto)).thenReturn(mockResponse);

        ResponseEntity<?> response = bannerController.updateBanner(bannerId, bannerDto);

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<BannerResponseAdmin> apiResponse = (ApiResponse<BannerResponseAdmin>) response.getBody();
        assertEquals(mockResponse, apiResponse.getData());
        assertEquals("Cập nhật banner thành công.", apiResponse.getMessage());
    }

    @Test
    void updateBanner() {
        Long bannerId = 1L;
        BannerDto bannerDto = new BannerDto();
        when(bannerService.updateBanner(bannerId, bannerDto)).thenReturn(null);

        ResponseEntity<?> response = bannerController.updateBanner(bannerId, bannerDto);

        assertEquals(400, response.getStatusCodeValue());
        ApiResponse<Void> apiResponse = (ApiResponse<Void>) response.getBody();
        assertEquals("Cập nhật banner thất bại.", apiResponse.getMessage());
    }




    @Test
    void deleteBanner() {
        Long bannerId = 1L;
        doNothing().when(bannerService).deleteBanner(bannerId);

        ResponseEntity<?> response = bannerController.deleteBanner(bannerId);

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<Void> apiResponse = (ApiResponse<Void>) response.getBody();
        assertEquals("Xóa banner thành công.", apiResponse.getMessage());
        verify(bannerService, times(1)).deleteBanner(bannerId);
    }

    @Test
    void setStageBanner() {
        Long bannerId = 1L;
        Integer stage = 1;
        doNothing().when(bannerService).changeStatusBanner(bannerId, stage);

        ResponseEntity<?> response = bannerController.setStageBanner(bannerId, stage);

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<Void> apiResponse = (ApiResponse<Void>) response.getBody();
        assertEquals("Cập nhật trạng thái banner thành công.", apiResponse.getMessage());
        verify(bannerService, times(1)).changeStatusBanner(bannerId, stage);
    }

    @Test
    void uploadImage() {
        Long bannerId = 1L;
        MultipartFile mockImage = mock(MultipartFile.class);
        doNothing().when(bannerService).uploadBannerImage(bannerId, mockImage);

        ResponseEntity<?> response = bannerController.uploadImage(bannerId, mockImage);

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<Void> apiResponse = (ApiResponse<Void>) response.getBody();
        assertEquals("Upload ảnh banner thành công.", apiResponse.getMessage());
        verify(bannerService, times(1)).uploadBannerImage(bannerId, mockImage);
    }
}
