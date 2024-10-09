package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.ChangePasswordDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.AccountUserResponse;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.repositories.UserRepository;
import com.be_planfortrips.services.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("${api.prefix}/users")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page) {
        try {
            Page<AccountUserResponse> users = this.userService.getAllUsersWithPagination(page);
            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiResponse.<Void>builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .message("Không có người dùng.")
                                .build()
                );
            }

            List<AccountUserResponse> userResponses = users.getContent();
            int totalPages = users.getTotalPages();
            long totalElements = users.getTotalElements();

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("totalElements", totalElements);
            responseMap.put("totalPages", totalPages);
            responseMap.put("userResponses", userResponses);

            return ResponseEntity.ok(
                    ApiResponse.<Map<String, Object>>builder()
                            .code(HttpStatus.OK.value())
                            .data(responseMap)
                            .message("Lấy danh sách người dùng thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Lấy danh sách người dùng thất bại.")
                            .build()
            );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            AccountUserResponse user = this.userService.createUser(userDto);

            return ResponseEntity.ok(
                    ApiResponse.<AccountUserResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(user)
                            .message("Tạo người dùng thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Tạo người dùng thất bại.")
                            .build()
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestParam("id") Long id, @Valid @RequestBody UserDto userDto) {
        try {
            AccountUserResponse user = this.userService.updateUser(id, userDto);

            return ResponseEntity.ok(
                    ApiResponse.<AccountUserResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(user)
                            .message("Cập nhật người dùng thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Cập nhật người dùng thất bại.")
                            .build()
            );
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id) {
        try {
            this.userService.deleteUser(id);

            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Xóa người dùng thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Xóa người dùng thất bại.")
                            .build()
            );
        }
    }

    @PatchMapping("/setStage")
    public ResponseEntity<?> updateStage(@RequestParam("id") Long id, @RequestParam("stage") Integer stage) {
        try {
            AccountUserResponse user = this.userService.updateStage(id, stage);

            return ResponseEntity.ok(
                    ApiResponse.<AccountUserResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(user)
                            .message("Cập nhật trạng thái người dùng thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Cập nhật trạng thái người dùng thất bại.")
                            .build()
            );
        }
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        try {
            this.userService.changePassword(changePasswordDto);

            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Đổi mật khẩu thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Đổi mật khẩu thất bại.")
                            .build()
            );
        }
    }

    @GetMapping("/getDetail")
    public ResponseEntity<?> getUserById(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "username", required = false) String username, @RequestParam(value = "email", required = false) String email){
        try {
            if(id == null && username == null && email == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ApiResponse.<Void>builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .message("Vui lòng nhập id, username hoặc email.")
                                .build()
                );
            }

            AccountUserResponse user;

            if(id != null) {
                user = this.userService.getUserByIdActive(id);
            } else if(username != null) {
                user = this.userService.getUserByUsername(username);
            } else {
                user = this.userService.getUserByEmail(email);
            }

            return ResponseEntity.ok(
                    ApiResponse.<AccountUserResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(user)
                            .message("Lấy thông tin người dùng thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Lấy thông tin người dùng thất bại.")
                            .build()
            );
        }
    }

    @PostMapping("upload")
    public ResponseEntity<?> uploadImage(@RequestParam("id") Long id, @RequestParam("file") MultipartFile file) {
        try {
            String filename = this.userService.uploadAvatar(id, file);

            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .code(HttpStatus.OK.value())
                            .data(filename)
                            .message("Upload ảnh thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Upload ảnh thất bại.")
                            .build()
            );
        }
    }


}
