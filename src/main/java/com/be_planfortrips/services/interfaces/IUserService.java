package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.ChangePasswordDto;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.dto.response.AccountUserResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IUserService {
    AccountUserResponse createUser(UserDto userDto);
    AccountUserResponse updateUser(UserDto userDto);
    void deleteUser(Long id);
    User getUserById(Long id);
    Map<String, Object> getAllUsersWithPagination(Integer page);
    void changePassword(ChangePasswordDto changePasswordDto);
    AccountUserResponse getUserByUsername(String username);
    AccountUserResponse getUserByEmail(String email);
    AccountUserResponse updateStage(Long id, Integer stage);
    AccountUserResponse getUserByIdActive(Long id);
    String uploadAvatar(MultipartFile file);
    Map<String, Object> getAvatar();
    AccountUserResponse getUserDetail();
    void verifyPassword(String password);
    List<AccountUserResponse> findByCarCompanyId(Integer id);
    List<AccountUserResponse> findByHotelId(Integer id);
    void resetPass(String email, String newPass);
}
