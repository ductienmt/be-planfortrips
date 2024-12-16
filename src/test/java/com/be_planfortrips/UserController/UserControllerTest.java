package com.be_planfortrips.UserController;

import com.be_planfortrips.controllers.UserController;
import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.ChangePasswordDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.AccountUserResponse;
import com.be_planfortrips.services.interfaces.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    private UserDto userDto;
    private AccountUserResponse accountUserResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDto = new UserDto();
        accountUserResponse = new AccountUserResponse(1L, "testUser", "123456789", "Male",
                "Address", true, null, "Full Name", "test@example.com", null);
    }

    @Test
    void testGetAllUsers() {
        Map<String, Object> users = new HashMap<>();
        users.put("total", 1);
        users.put("users", List.of(accountUserResponse));

        when(userService.getAllUsersWithPagination(1)).thenReturn(users);

        ResponseEntity<?> response = userController.getAllUsers(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals(users, apiResponse.getData());
        assertEquals("Lấy danh sách người dùng thành công.", apiResponse.getMessage());
    }

    @Test
    void testCreateUser() {
        when(userService.createUser(any(UserDto.class))).thenReturn(accountUserResponse);

        ResponseEntity<?> response = userController.createUser(userDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals(accountUserResponse, apiResponse.getData());
        assertEquals("Tạo người dùng thành công.", apiResponse.getMessage());
    }

    @Test
    void testUpdateUser() {
        when(userService.updateUser(any(UserDto.class))).thenReturn(accountUserResponse);

        ResponseEntity<?> response = userController.updateUser(userDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals(accountUserResponse, apiResponse.getData());
        assertEquals("Cập nhật người dùng thành công.", apiResponse.getMessage());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<?> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals("Xóa người dùng thành công.", apiResponse.getMessage());
    }

    @Test
    void testChangePassword() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();

        doNothing().when(userService).changePassword(any(ChangePasswordDto.class));

        ResponseEntity<?> response = userController.changePassword(changePasswordDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals("Đổi mật khẩu thành công.", apiResponse.getMessage());
    }

    @Test
    void testVerifyPassword() {
        String password = "testPassword";

        doNothing().when(userService).verifyPassword(password);

        ResponseEntity<?> response = userController.verifyEmail(password);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals("Xác thực mật khẩu thành công.", apiResponse.getMessage());
    }

    @Test
    void testGetUserDetail() {
        when(userService.getUserDetail()).thenReturn(accountUserResponse);

        ResponseEntity<?> response = userController.getUserDetail();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals(accountUserResponse, apiResponse.getData());
        assertEquals("Lấy thông tin người dùng thành công.", apiResponse.getMessage());
    }

    @Test
    void testUploadImage() {
        String filename = "uploaded_image.png";
        when(userService.uploadAvatar(any())).thenReturn(filename);

        ResponseEntity<?> response = userController.uploadImage(mock(MultipartFile.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals(filename, apiResponse.getData());
        assertEquals("Upload ảnh thành công.", apiResponse.getMessage());
    }

    @Test
    void testGetImage() {
        Map<String, Object> imageData = new HashMap<>();
        imageData.put("image", "image_url");
        when(userService.getAvatar()).thenReturn(imageData);

        ResponseEntity<?> response = userController.getImage();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
        assertEquals(imageData, apiResponse.getData());
        assertEquals("Lấy ảnh thành công.", apiResponse.getMessage());
    }

    // Add additional tests for methods like findByUsername, findByCarCompanyId, etc.
}