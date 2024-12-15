package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.response.AccountEnterpriseResponse;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.AccountEnterpriseMapper;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.ImageRepository;
import com.be_planfortrips.repositories.RoleRepository;
import com.be_planfortrips.services.interfaces.IAccountEnterpriseService;
import com.be_planfortrips.services.interfaces.IEmailService;
import com.be_planfortrips.utils.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountEnterpriseServiceImpl implements IAccountEnterpriseService {

    // Inject repository and mapper
    AccountEnterpriseRepository accountEnterpriseRepository;
    AccountEnterpriseMapper accountEnterpriseMapper;
    IEmailService iEmailService;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    TypeEnterpriseDetailServiceImpl typeEnterpriseDetailService;
    TokenMapperImpl tokenMapper;
    private final TokenMapperImpl tokenMapperImpl;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    @Override
    public Page<AccountEnterpriseResponse> getAllAccountEnterprises(String name, int page, int size) {

        Page<AccountEnterprise> accountEnterprisesPage;
        if (name != null && !name.isEmpty()) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
            accountEnterprisesPage = accountEnterpriseRepository.findByEnterpriseNameContainingIgnoreCase(name, pageable);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
            accountEnterprisesPage = accountEnterpriseRepository.findAll(pageable);
        }

        return accountEnterprisesPage.map(accountEnterpriseMapper::toResponse);
    }

    private String normalizeString(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    @Override
    public AccountEnterpriseResponse getAccountEnterpriseById(Long id) {
        Optional<AccountEnterprise> accountEnterprise = accountEnterpriseRepository.findById(id);
        System.out.println(accountEnterprise.isEmpty());
        return accountEnterpriseMapper.toResponse(accountEnterprise.get());
    }

    @Override
    public AccountEnterpriseResponse createAccountEnterprise(AccountEnterpriseDto accountEnterpriseDto) {
        validateForm(accountEnterpriseDto);
        // Chuyển đổi DTO thành entity và lưu vào repository
        AccountEnterprise accountEnterprise = accountEnterpriseMapper.toEntity(accountEnterpriseDto);
        String passwordDefault = accountEnterpriseDto.getUsername()+"@"+generateRandomString(5);;
        accountEnterprise.setPassword(passwordEncoder.encode(passwordDefault));
        iEmailService.sendEmail(accountEnterpriseDto.getEmail(),
                passwordDefault,"Vui lòng đăng nhập và thay đổi mật khẩu mặc định"
                );
        accountEnterprise.setStatus(false);
        accountEnterprise.setRole(roleRepository.findById(3L).orElseThrow(() -> new RuntimeException("Không tìm thấy role")));
        accountEnterprise = accountEnterpriseRepository.save(accountEnterprise);
        return accountEnterpriseMapper.toResponse(accountEnterprise);
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        // Lặp để tạo chuỗi
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }

    @Override
    public AccountEnterpriseResponse updateAccountEnterprise(AccountEnterpriseDto accountEnterpriseDto) {
        // Tìm tài khoản doanh nghiệp theo ID
        AccountEnterprise accountEnterprise = accountEnterpriseRepository.findById(tokenMapper.getIdEnterpriseByToken())
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        if (accountEnterpriseDto.getPhoneNumber() != null && !accountEnterpriseDto.getPhoneNumber().isEmpty()) {
            if (!Utils.isValidPhoneNumber(accountEnterpriseDto.getPhoneNumber())) {
                throw new AppException(ErrorType.phoneNotValid);
            }
        }

        if(accountEnterpriseDto.getEmail() != null && !accountEnterpriseDto.getEmail().isEmpty()){
            if(!Utils.isValidEmail(accountEnterpriseDto.getEmail())){
                throw new AppException(ErrorType.emailNotValid);
            }
        }

        for (Field field : AccountEnterpriseDto.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object newValue = field.get(accountEnterpriseDto);

                if (newValue != null) {
                    Field serviceField = AccountEnterprise.class.getDeclaredField(field.getName());
                    serviceField.setAccessible(true);
                    serviceField.set(accountEnterprise, newValue);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException("Error accessing field: " + field.getName(), e);
            }
        }



        this.accountEnterpriseRepository.save(accountEnterprise);
        return this.accountEnterpriseMapper.toResponse(accountEnterprise);
    }

    @Override
    public void deleteAccountEnterpriseById(Long id) {
        // Tìm tài khoản doanh nghiệp theo ID
        AccountEnterprise accountEnterprise = accountEnterpriseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound)); // Nếu không tìm thấy thì ném lỗi
        accountEnterprise.setStatus(false);
        // Xóa tài khoản doanh nghiệp
        accountEnterpriseRepository.save(accountEnterprise);
    }

    @Override
    public AccountEnterpriseResponse getAccountEnterpriseDetail() {
        return this.getAccountEnterpriseById(tokenMapper.getIdEnterpriseByToken());
    }

 @Override
    public Boolean toggleStage(Long userId) {
        AccountEnterprise accountEnterprise = accountEnterpriseRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorType.notFound)
        );

        if (accountEnterprise.isStatus()) {
            accountEnterprise.setStatus(false);
        }
        else {
            accountEnterprise.setStatus(true);
        }

        accountEnterpriseRepository.save(accountEnterprise);

        return accountEnterprise.isStatus();
    }

    @Override
    public void validateUsername(String username) {
        boolean exists = accountEnterpriseRepository.existsByUsername(username);
        if (exists) {
            throw new AppException(ErrorType.usernameExisted);
        }
    }

    @Override
    public void validateEmail(String email) {
        boolean exists = accountEnterpriseRepository.existsByEmail(email);
        if (exists) {
            throw new AppException(ErrorType.emailExisted);
        }
    }

    @Override
    public void validatePhone(String phone) {
        boolean exists = accountEnterpriseRepository.existsByPhoneNumber(phone);
        if (exists) {
            throw new AppException(ErrorType.phoneExisted);
        }
    }

    @Override
    public List<AccountEnterpriseResponse> getAccountEnterpriseDisable() {
        return accountEnterpriseRepository.findAccountEnterpriseDisable().stream().map(accountEnterpriseMapper::toResponse).toList();
    }

    @Override
    public void resetPassword(Integer id, String email, String phone) {
        // Kiểm tra loại dịch vụ có tồn tại
        Optional<AccountEnterprise> accountEnterpriseOptional = accountEnterpriseRepository.findByServiceTypeAndEmailAndPhone(id, email, phone);

        if (accountEnterpriseOptional.isPresent()) {
            // Random mật khẩu mới
            String newPassword = generateRandomString(10);
            AccountEnterprise accountEnterprise = accountEnterpriseOptional.get();

            // Mã hóa mật khẩu trước khi lưu
            accountEnterprise.setPassword(passwordEncoder.encode(newPassword));

            // Lưu tài khoản vào cơ sở dữ liệu
            accountEnterpriseRepository.save(accountEnterprise);

            // Gửi email thông báo mật khẩu mới
            iEmailService.sendEmail(email, newPassword, "Mật khẩu mới từ Plan for Trips");
        } else {
            throw new AppException(ErrorType.notFound);
        }
    }


    @Override
    public boolean validateContact(Integer serviceType, String email, String phone) {
        // Lấy danh sách doanh nghiệp theo serviceType
        List<AccountEnterprise> enterprises = accountEnterpriseRepository.findActiveByServiceType(serviceType);

        // Kiểm tra xem email và phone có tồn tại trong danh sách doanh nghiệp không
        return enterprises.stream()
                .anyMatch(e -> e.getEmail().equals(email) && e.getPhoneNumber().equals(phone));
    }

    @Override
    public List<AccountEnterpriseResponse> getAccountEnterpriseNeedAccept() {
        return accountEnterpriseRepository.findAccountEnterpriseNeedAccept().stream().map(accountEnterpriseMapper::toResponse).toList();
    }

    @Override
    public AccountEnterpriseResponse getAccountEnterpriseByPhoneNumber(String phoneNumber) {
        AccountEnterprise accountEnterprise = accountEnterpriseRepository.getAccountEnterpriseByPhoneNumber(phoneNumber).orElseThrow(
                () -> new AppException(ErrorType.PhoneNumberNotExist)
        );
        return accountEnterpriseMapper.toResponse(accountEnterprise);
    }

    @Override
    public AccountEnterpriseResponse getAccountEnterpriseByEmail(String email) {
        AccountEnterprise accountEnterprise = accountEnterpriseRepository.getAccountEnterpriseByEmail(email).orElseThrow(
                () -> new AppException(ErrorType.EmailNotExist)
        );
        return accountEnterpriseMapper.toResponse(accountEnterprise);
    }

    @Override
    @Transactional
    public void uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ảnh hợp lệ");
        }

        Optional<AccountEnterprise> userOptional = accountEnterpriseRepository.findById(tokenMapperImpl.getIdEnterpriseByToken());
        if (userOptional.isEmpty()) {
            throw new AppException(ErrorType.notFound, "Người dùng không tồn tại");
        }

        AccountEnterprise user = userOptional.get();

        Utils.checkSize(file);

        String avatarUrl;
        try {
            // Nếu user đã có ảnh, xóa ảnh cũ khỏi Cloudinary và csdl
            if (user.getImage() != null) {
                Image currentImage = user.getImage();
                String publicId = cloudinaryService.getPublicIdFromUrl(currentImage.getUrl());
                cloudinaryService.deleteFile(publicId);
                imageRepository.delete(currentImage);
            }

            Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, "avatars_enterprise");
            avatarUrl = uploadResult.get("url").toString();

            Image newImage = new Image();
            newImage.setUrl(avatarUrl);
            imageRepository.saveAndFlush(newImage);

            user.setImage(newImage);
            accountEnterpriseRepository.saveAndFlush(user);

        } catch (IOException e) {
            throw new AppException(ErrorType.internalServerError, "Lỗi khi tải ảnh lên");
        }
    }

    @Override
    public void verifyPassword(String password) {
        Optional<AccountEnterprise> user = this.accountEnterpriseRepository.findById(tokenMapperImpl.getIdEnterpriseByToken());
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new AppException(ErrorType.notMatchPassword);
        }
    }


    private void validateForm(AccountEnterpriseDto accountEnterpriseDto) {
        if (accountEnterpriseDto.getUsername() == null || accountEnterpriseDto.getUsername().isEmpty()) {
            throw new RuntimeException("Username không được để trống.");
        } else if (accountEnterpriseDto.getEnterpriseName() == null || accountEnterpriseDto.getEnterpriseName().isEmpty()) {
            throw new RuntimeException("Tên doanh nghiệp không được để trống.");
        } else if (accountEnterpriseDto.getEmail() == null || accountEnterpriseDto.getEmail().isEmpty()) {
            throw new RuntimeException("Email không hợp lệ.");
        } else if (accountEnterpriseDto.getPhoneNumber() == null || accountEnterpriseDto.getPhoneNumber().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống.");
        } else if (accountEnterpriseDto.getAddress() == null || accountEnterpriseDto.getAddress().isEmpty()) {
            throw new RuntimeException("Địa chỉ không được để trống.");
        } else if (accountEnterpriseDto.getTypeEnterpriseDetailId() == null) {
            throw new RuntimeException("Loại hình doanh nghiệp không được để trống.");
        } else if (accountEnterpriseDto.getRepresentative() == null || accountEnterpriseDto.getRepresentative().isEmpty()) {
            throw new RuntimeException("Người đại diện doanh nghiệp không được để trống.");
        } else if (!Utils.isValidPhoneNumber(accountEnterpriseDto.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại không hợp lệ.");
        }
        if (accountEnterpriseRepository.findByUsername(accountEnterpriseDto.getUsername()) != null) {
            throw new AppException(ErrorType.usernameExisted);
        }
    }
}
