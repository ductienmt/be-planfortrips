package com.be_planfortrips.Test.anhquan;

import com.be_planfortrips.entity.City;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.repositories.CityRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.services.impl.CityArea;
import com.be_planfortrips.services.impl.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityAreaTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private CityArea cityArea;

    private City mockCity;

    @BeforeEach
    void setUp() {
        mockCity = new City();
        mockCity.setId("1");
        mockCity.setNameCity("Hanoi");
        mockCity.setDescription("Capital city of Vietnam");
    }

    @Test
    void testGetCityByAreaId() {
        when(cityRepository.findByArea_Id("areaId")).thenReturn(List.of(mockCity));

        List<City> cities = cityArea.getCityByAreaId("areaId");

        verify(cityRepository, times(1)).findByArea_Id("areaId");
        assertNotNull(cities);
        assertEquals(1, cities.size());
        assertEquals("Hanoi", cities.get(0).getNameCity());
    }
    @Test
    void testGetAllCity() {
        when(cityRepository.findAll()).thenReturn(List.of(mockCity));

        List<Map<String, Object>> cityList = cityArea.getAllCity();

        verify(cityRepository, times(1)).findAll();
        assertNotNull(cityList);
        assertEquals(1, cityList.size());
        assertEquals("Hanoi", cityList.get(0).get("nameCity"));
    }

    @Test
    void testUploadImage_Success() throws IOException {
        Image image = new Image();
        image.setUrl("http://image.url");

        when(file.isEmpty()).thenReturn(false);
        when(cloudinaryService.uploadFile(file, "city_images")).thenReturn(Map.of("url", "http://image.url"));
        when(cityRepository.findById("1")).thenReturn(Optional.of(mockCity));
        when(imageRepository.saveAndFlush(any(Image.class))).thenReturn(image);

        cityArea.uploadImage(file, "1");

        verify(imageRepository, times(1)).saveAndFlush(any(Image.class));
        verify(cityRepository, times(1)).saveAndFlush(mockCity);
    }

    @Test
    void testUploadImage_FileEmpty() {
        when(file.isEmpty()).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cityArea.uploadImage(file, "1"));
        assertEquals("Vui lòng chọn ảnh hợp lệ", exception.getMessage());

        verify(file, times(1)).isEmpty();
        verifyNoInteractions(cloudinaryService, cityRepository, imageRepository);
    }

    @Test
    void testUploadImage_CityNotFound() {
        when(cityRepository.findById("invalid")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> cityArea.uploadImage(file, "invalid"));
        assertEquals(ErrorType.notFound, exception.getErrorType());

        verify(cityRepository, times(1)).findById("invalid");
        verifyNoInteractions(cloudinaryService, imageRepository);
    }

    @Test
    void testFindCityByName() {
        when(cityRepository.searchByNameCityContaining("Hanoi")).thenReturn(List.of(mockCity));

        List<String> cityNames = cityArea.findCityByName("Hanoi");

        verify(cityRepository, times(1)).searchByNameCityContaining("Hanoi");
        assertNotNull(cityNames);
        assertEquals(1, cityNames.size());
        assertTrue(cityNames.contains("Hanoi"));
    }

    @Test
    void testGetCityByAreaId_Fail() {
        // Giả sử khi tìm kiếm theo areaId, không có thành phố nào được trả về
        when(cityRepository.findByArea_Id("invalidAreaId")).thenReturn(Collections.emptyList());

        // Gọi phương thức để kiểm tra
        List<City> cities = cityArea.getCityByAreaId("invalidAreaId");

        // Xác minh rằng phương thức trả về danh sách trống
        assertNotNull(cities);
        assertTrue(cities.isEmpty());

        // Xác minh rằng phương thức `findByArea_Id` đã được gọi đúng 1 lần với tham số "invalidAreaId"
        verify(cityRepository, times(1)).findByArea_Id("invalidAreaId");
    }


    @Test
    void testFindCityByName_Fail() {
        // Giả sử khi tìm kiếm theo tên thành phố, không có kết quả nào trả về
        when(cityRepository.searchByNameCityContaining("NonExistentCity")).thenReturn(Collections.emptyList());

        // Gọi phương thức để kiểm tra
        List<String> cityNames = cityArea.findCityByName("NonExistentCity");

        // Xác minh rằng phương thức trả về danh sách trống
        assertNotNull(cityNames);
        assertTrue(cityNames.isEmpty());

        // Xác minh rằng phương thức `searchByNameCityContaining` đã được gọi đúng 1 lần
        verify(cityRepository, times(1)).searchByNameCityContaining("NonExistentCity");
    }


}
