package com.be_planfortrips.Test.anhquan;

import com.be_planfortrips.entity.Admin;
import com.be_planfortrips.repositories.AdminRepository;
import com.be_planfortrips.services.impl.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Mockito extension tự động khởi tạo các mock
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void testFindAdminByUsername_Success() {
        // Arrange
        String userName = "adminUser";
        Admin admin = new Admin();
        admin.setUserName(userName);
        admin.setPassword("securePassword");

        // Mock hành vi của repository để trả về một Admin
        when(adminRepository.findByUsername(userName)).thenReturn(admin);

        // Act
        Admin result = adminService.findAdminByUsername(userName);

        // Assert
        assertNotNull(result);  // Kiểm tra nếu kết quả không phải null
        assertEquals(userName, result.getUserName());  // Kiểm tra nếu username trùng khớp
        verify(adminRepository, times(1)).findByUsername(userName);  // Kiểm tra rằng phương thức được gọi đúng một lần
    }

    @Test
    void testFindAdminByUsername_NotFound() {
        // Arrange
        String userName = "nonExistentUser";

        // Mock hành vi của repository để trả về null nếu không tìm thấy admin
        when(adminRepository.findByUsername(userName)).thenReturn(null);

        // Act
        Admin result = adminService.findAdminByUsername(userName);

        // Assert
        assertNull(result);  // Kết quả phải là null nếu không tìm thấy admin
        verify(adminRepository, times(1)).findByUsername(userName);  // Kiểm tra rằng phương thức được gọi đúng một lần
    }

    @Test
    void testFindAdminByUsername_NullInput() {
        // Arrange
        when(adminRepository.findByUsername(null)).thenReturn(null);  // Mock để trả về null nếu input là null

        // Act
        Admin result = adminService.findAdminByUsername(null);

        // Assert
        assertNull(result);  // Kiểm tra kết quả là null khi input là null
        verify(adminRepository, times(1)).findByUsername(null);  // Kiểm tra rằng phương thức được gọi đúng một lần
    }
}
