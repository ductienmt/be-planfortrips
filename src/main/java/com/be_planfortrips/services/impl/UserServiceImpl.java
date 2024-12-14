package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.ChangePasswordDto;
import com.be_planfortrips.entity.CarCompany;
import com.be_planfortrips.entity.Hotel;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.mappers.impl.UserMapper;
import com.be_planfortrips.repositories.*;
import com.be_planfortrips.dto.response.AccountUserResponse;
import com.be_planfortrips.services.interfaces.IUserService;
import com.be_planfortrips.utils.Utils;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarCompanyRepository carCompanyRepository;
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Utils utils;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TokenMapperImpl tokenMapperImpl;

    @Override
    public AccountUserResponse createUser(UserDto userDto) {
        try {
            User user = this.userRepository.findByUsername(userDto.getUserName());
            if (user != null) {
                throw new AppException(ErrorType.usernameExisted);
            }

            if (!this.utils.isValidEmail(userDto.getEmail())) {
                throw new RuntimeException("Email không hợp lệ");
            }

            if (!this.utils.isValidPhoneNumber(userDto.getPhoneNumber())) {
                throw new RuntimeException("Số điện thoại không hợp lệ");
            }

            User newUser = this.userMapper.toEntity(userDto);
            newUser.setRole(roleRepository.findById(1L).orElseThrow(() -> new RuntimeException("Không tìm thấy role với id: 1")));
            newUser.setActive(true);
            newUser.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
            this.userRepository.save(newUser);
            return this.userMapper.toResponse(newUser);

        } catch (DataIntegrityViolationException e) {
            // Kiểm tra nếu lỗi vi phạm ràng buộc duy nhất (unique constraint)
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new AppException(ErrorType.usernameExisted);
            }
            // Các trường hợp vi phạm dữ liệu khác
            throw new RuntimeException("Có lỗi xảy ra khi tạo tài khoản, vui lòng thử lại.");
        } catch (Exception e) {
            // Bắt các lỗi khác
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public AccountUserResponse updateUser(UserDto userDto) {
        User user = this.userRepository.findById(tokenMapperImpl.getIdUserByToken())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + tokenMapperImpl.getIdUserByToken()));

        if (userDto.getUserName() != null && !userDto.getUserName().equals(user.getUserName()) &&
                this.userRepository.findByUsername(userDto.getUserName()) != null) {
            throw new RuntimeException("Username đã tồn tại, vui lòng đổi username khác");
        }

        for (Field field : UserDto.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object newValue = field.get(userDto);

                if (newValue != null) {
                    Field serviceField = User.class.getDeclaredField(field.getName());
                    serviceField.setAccessible(true);
                    Object currentValue = serviceField.get(user);

                    if (!newValue.equals(currentValue)) {
                        serviceField.set(user, newValue);
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException("Error accessing field: " + field.getName(), e);
            }
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        }

        this.userRepository.save(user);
        return this.userMapper.toResponse(user);
    }


    @Override
    public void deleteUser(Long id) {
        User user = this.getUserById(id);
        user.setActive(false);
        this.userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
    }

    @Override
    public Map<String, Object> getAllUsersWithPagination(Integer page) {
        int size = 10;

        if (page < 1) {
            throw new IllegalArgumentException("Trang không hợp lệ, phải từ 1 trở lên");
        }

        Page<User> users = this.userRepository.findAll(PageRequest.of(page - 1, size));

        List<AccountUserResponse> userResponses = users.map(user -> this.userMapper.toResponse(user)).getContent();

        int totalPages = users.getTotalPages();
        long totalElements = users.getTotalElements();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("totalElements", totalElements);
        responseMap.put("totalPages", totalPages);
        responseMap.put("userResponses", userResponses);
        if (users.isEmpty()) {
            throw new AppException(ErrorType.notFound);
        }
        return responseMap;
    }


    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        User user = this.getUserById(tokenMapperImpl.getIdUserByToken());

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorType.notMatchPassword);
        }

        user.setPassword(this.passwordEncoder.encode(changePasswordDto.getNewPassword()));
        this.userRepository.save(user);
    }

    @Override
    public AccountUserResponse getUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Tài khoản có thể bị khóa hoặc chưa tạo tài khoản");
        }
        return this.userMapper.toResponse(user);
    }

    @Override
    public AccountUserResponse getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Tài khoản có thể bị khóa hoặc chưa tạo tài khoản");
        }
        return this.userMapper.toResponse(user);
    }

    @Override
    public AccountUserResponse updateStage(Long id, Integer stage) {
        User user = this.getUserById(id);
        if (stage > 1 || stage < 0) {
            throw new RuntimeException("Trạng thái không hợp lệ");
        } else {
            if (stage == 1) {
                user.setActive(true);
                this.userRepository.saveAndFlush(user);
            } else {
                user.setActive(false);
                this.userRepository.saveAndFlush(user);
            }
        }
        return this.userMapper.toResponse(user);
    }

    @Override
    public AccountUserResponse getUserByIdActive(Long id) {
        User user = this.userRepository.findByIdActive(id);
        if (user == null) {
            throw new RuntimeException("Taì khoản không tồn tại hoặc có thể bị khóa");
        }
        return this.userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public String uploadAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ảnh hợp lệ");
        }

        User user = this.getUserById(tokenMapperImpl.getIdUserByToken());

        this.utils.isValidImage(file);

        String avatarUrl;
        try {
            if (user.getImage() != null) {
                Image currentImage = user.getImage();
                String publicId = cloudinaryService.getPublicIdFromUrl(currentImage.getUrl());
                cloudinaryService.deleteFile(publicId);
                imageRepository.delete(currentImage);
            }

            Map<String, Object> uploadResult = this.cloudinaryService.uploadFile(file, "avatars_user");
            avatarUrl = uploadResult.get("url").toString();

            Image newImage = new Image();
            newImage.setUrl(avatarUrl);
            this.imageRepository.saveAndFlush(newImage);

            user.setImage(newImage);
            this.userRepository.saveAndFlush(user);

        } catch (IOException e) {
            throw new AppException(ErrorType.internalServerError, "Lỗi khi tải ảnh lên");
        }

        return avatarUrl;
    }


    @Override
    public Map<String, Object> getAvatar() {
        Map<String, Object> responseMap = new HashMap<>();
        Long userId = tokenMapperImpl.getIdUserByToken();
        String url = this.userRepository.getAvatarUser(userId);
        User user = this.getUserById(userId);
        if (url == null) {
            responseMap.put("fullname", user.getFullName());
            responseMap.put("url", "");
            responseMap.put("gender", user.getGender());
        } else {
            responseMap.put("url", url);
            responseMap.put("fullname", user.getFullName());
            responseMap.put("gender", user.getGender());
        }
        return responseMap;
    }

    @Override
    public AccountUserResponse getUserDetail() {
        User user = this.userRepository.findById(tokenMapperImpl.getIdUserByToken())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return this.userMapper.toResponse(user);
    }

    @Override
    public void verifyPassword(String password) {
        User user = this.getUserById(tokenMapperImpl.getIdUserByToken());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorType.notMatchPassword);
        }
    }

    @Override
    public List<AccountUserResponse> findByCarCompanyId(Integer id) {
        CarCompany carCompany = carCompanyRepository.findById(id)
                .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        List<AccountUserResponse> userResponses = userRepository.findUserByCarCompanyId(carCompany.getId())
                .stream()
                .map(
                        userMapper::toResponse
                ).collect(Collectors.toList());
        return userResponses;
    }

    @Override
    public List<AccountUserResponse> findByHotelId(Integer id) {
        Hotel hotel = hotelRepository.findById(Long.valueOf(id))
                .orElseThrow(()->{throw new AppException(ErrorType.notFound);});
        List<AccountUserResponse> userResponses = userRepository.findUserByHotelId(hotel.getId())
                .stream()
                .map(
                        userMapper::toResponse
                ).collect(Collectors.toList());
        return userResponses;
    }

    @Override
    public void resetPass(String email, String newPass) {
        // tìm tài khoản user bằng email trước
        // dùng cái trong repository có sẵn
        // tìm email nhưng mà tài khoản đó còn hoạt động
        User user = userRepository.findByEmail(email);
        // check user có null không
        // báo lỗi bằng cái này
        if (user == null) throw new AppException(ErrorType.userIdNotFound);

        // encode new pass bằng bcrypt
        String passEncode = passwordEncoder.encode(newPass);

        // chỉ set pass ko thay đổi cái khác
        user.setPassword(passEncode);

        // set xong thì lưu lại
        userRepository.save(user);

    }
}