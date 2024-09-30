package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.ChangePasswordDto;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.responses.AccountUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    AccountUserResponse createUser(UserDto userDto);
    AccountUserResponse updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    User getUserById(Long id);
    Page<AccountUserResponse> getAllUsersWithPagination(Integer page);
    void changePassword(ChangePasswordDto changePasswordDto);
    AccountUserResponse getUserByUsername(String username);
    AccountUserResponse getUserByEmail(String email);
    AccountUserResponse updateStage(Long id, Integer stage);
    AccountUserResponse getUserByIdActive(Long id);
    String uploadAvatar(Long userId, MultipartFile file);
}
