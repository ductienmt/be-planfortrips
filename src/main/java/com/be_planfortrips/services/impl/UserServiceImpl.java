package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.ChangePasswordDto;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.mappers.impl.UserMapper;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.repositories.UserRepository;
import com.be_planfortrips.dto.response.AccountUserResponse;
import com.be_planfortrips.services.interfaces.IUserService;
import com.be_planfortrips.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Utils utils;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public AccountUserResponse createUser(UserDto userDto) {
        User user = this.userRepository.findByUsername(userDto.getUserName());
        if (user != null) {
            throw new RuntimeException("Username đã tồn tại, vui lòng đổi username khác");
        } else {
            if (!this.utils.isValidEmail(userDto.getEmail())) {
                throw new RuntimeException("Email không hợp lệ");
            }
            if (!this.utils.isValidPhoneNumber(userDto.getPhoneNumber())) {
                throw new RuntimeException("Số điện thoại không hợp lệ");
            }
            User newUser = this.userMapper.toEntity(userDto);
            this.userRepository.save(newUser);
            return this.userMapper.toResponse(newUser);
        }
    }

    @Override
    public AccountUserResponse updateUser(Long id, UserDto userDto) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
        if (this.userRepository.findByUsername(userDto.getUserName()) != null) {
            throw new RuntimeException("Username đã tồn tại, vui lòng đổi username khác");
        } else {
            this.userMapper.updateEntityFromDto(userDto, user);
            this.userRepository.saveAndFlush(user);
            return this.userMapper.toResponse(user);
        }
    }

    @Override
    public void deleteUser(Long id) {
        User user = this.getUserById(id);
        this.userRepository.delete(user);
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
    }

    @Override
    public Page<AccountUserResponse> getAllUsersWithPagination(Integer page) {
        int size = 10;
        if (page < 1) {
            throw new RuntimeException("Trang không hợp lệ phải từ 1 trở lên");
        }
        Page<User> users = this.userRepository.findAll(PageRequest.of(page-1, size));
        return users.map(user -> this.userMapper.toResponse(user));
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        User user = this.getUserById(changePasswordDto.getId());

        if (!user.getPassword().equals(changePasswordDto.getOldPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        user.setPassword(changePasswordDto.getNewPassword());
        this.userRepository.saveAndFlush(user);
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
        if (user == null){
            throw new RuntimeException("Taì khoản không tồn tại hoặc có thể bị khóa");
        }
        return this.userMapper.toResponse(user);
    }

    @Override
    public String uploadAvatar(Long userId, MultipartFile file) {
        if(file == null || file.isEmpty()) {
            throw new RuntimeException("Vui lòng chọn ảnh");
        }
        User user = this.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("Tài khoản không tồn tại");
        }
        this.utils.isValidImage(file);
        this.utils.checkSize(file);

        String avatar = this.utils.saveImage(file);
        System.out.println(avatar);

        Image image = new Image();
        image.setUrl(avatar);
        this.imageRepository.saveAndFlush(image);
        user.setImage(image);
        this.userRepository.saveAndFlush(user);


        return image.getUrl();
    }


}
