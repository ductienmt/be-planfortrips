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
import jakarta.servlet.http.HttpServletRequest;
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

    // Phương thức hỗ trợ để tạo ApiResponse
    private ResponseEntity<ApiResponse<Void>> buildApiResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                ApiResponse.<Void>builder()
                        .code(status.value())
                        .message(message)
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page) {
        try {
            Map<String, Object> users = this.userService.getAllUsersWithPagination(page);

            return ResponseEntity.ok(
                    ApiResponse.<Map<String, Object>>builder()
                            .code(HttpStatus.OK.value())
                            .data(users)
                            .message("Lấy danh sách người dùng thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.NO_CONTENT, "Lấy danh sách người dùng thất bại.")
                    ;
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
            return buildApiResponse(HttpStatus.METHOD_FAILURE, "Tạo người dùng thất bại.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        try {
            AccountUserResponse user = this.userService.updateUser(userDto);

            return ResponseEntity.ok(
                    ApiResponse.<AccountUserResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(user)
                            .message("Cập nhật người dùng thành công.")
                            .build()
            );
        } catch (Exception e) {
            return buildApiResponse(HttpStatus.METHOD_FAILURE, e.getMessage());
        }
    }

    @PostMapping("/delete")
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
            return buildApiResponse(HttpStatus.METHOD_FAILURE, "Xóa người dùng thất bại.");
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
            return buildApiResponse(HttpStatus.METHOD_FAILURE, "Cập nhật trạng thái người dùng thất bại.");
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
            return buildApiResponse(HttpStatus.METHOD_FAILURE, "Đổi mật khẩu thất bại.");
        }
    }

    @GetMapping("/verify-password")
    public ResponseEntity<?> verifyEmail(@RequestParam("password") String pass) {
        try {
            this.userService.verifyPassword(pass);

            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Xác thực mật khẩu thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.UNAUTHORIZED, "Xác thực mật khẩu thất bại.");
        }
    }


    @GetMapping("/getDetail")
    public ResponseEntity<?> getUserById(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "username", required = false) String username, @RequestParam(value = "email", required = false) String email) {
        try {
            if (id == null && username == null && email == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ApiResponse.<Void>builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .message("Vui lòng nhập id, username hoặc email.")
                                .build()
                );
            }

            AccountUserResponse user;

            if (id != null) {
                user = this.userService.getUserByIdActive(id);
            } else if (username != null) {
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
            return buildApiResponse(HttpStatus.NO_CONTENT, "Lấy thông tin người dùng thất bại.");
        }
    }

    @PostMapping("upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String filename = this.userService.uploadAvatar(file);

            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .code(HttpStatus.OK.value())
                            .data(filename)
                            .message("Upload ảnh thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Upload ảnh thất bại.");
        }
    }

    @GetMapping("getImage")
    public ResponseEntity<?> getImage() {
        try {
            return ResponseEntity.ok(
                    ApiResponse.<Map<String, Object>>builder()
                            .code(HttpStatus.OK.value())
                            .data(this.userService.getAvatar())
                            .message("Lấy ảnh thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.NO_CONTENT, "Lấy ảnh thất bại.");
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getUserDetail() {
        try {
            AccountUserResponse user = this.userService.getUserDetail();

            return ResponseEntity.ok(
                    ApiResponse.<AccountUserResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(user)
                            .message("Lấy thông tin người dùng thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.NOT_FOUND, "Lấy thông tin người dùng thất bại.");
        }
    }

    @GetMapping("/findByUsername")
    public ResponseEntity<?> findByUserName(@RequestParam("username") String username) {
        try {
            AccountUserResponse user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findByCarCompany/{id}")
    public ResponseEntity<?> findByCarCompanyId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(userService.findByCarCompanyId(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findByHotel/{id}")
    public ResponseEntity<?> findByHotelId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(userService.findByHotelId(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/resetPassword")
    public ResponseEntity<?> changePassword(
            @RequestParam String email,
            @RequestParam String newPass
            ) {
        try {
            // lấy từ param xuống email với newPass
            // làm hàm resetPass email xác định tài khoản user
            // nếu có thì encode newPass và set dô rồi lưu lại
            // nếu ko có thất bại
            userService.resetPass(email, newPass);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Đổi mật khẩu thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.METHOD_FAILURE, "Đổi mật khẩu thất bại.");
        }
    }

    @GetMapping("getUserByEmail")
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email) {
        AccountUserResponse user = userService.getUserByEmail(email);
        if (user == null) {
            throw new AppException(ErrorType.notFound);
        }
        return buildApiResponse(HttpStatus.OK, "Xác thực thành công.");
    }
}
