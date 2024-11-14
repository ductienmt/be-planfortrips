package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.TypeEnterpriseDetailDto;
import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.response.AccountEnterpriseResponse;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.TokenMapper;
import com.be_planfortrips.mappers.impl.AccountEnterpriseMapper;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.RoleRepository;
import com.be_planfortrips.services.interfaces.IAccountEnterpriseService;
import com.be_planfortrips.utils.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountEnterpriseServiceImpl implements IAccountEnterpriseService {

    // Inject repository and mapper
    AccountEnterpriseRepository accountEnterpriseRepository;
    AccountEnterpriseMapper accountEnterpriseMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    TypeEnterpriseDetailServiceImpl typeEnterpriseDetailService;
    TokenMapperImpl tokenMapper;

    @Override
    public List<AccountEnterpriseResponse> getAllAccountEnterprises(int page, int size) {
        // Tạo một Pageable với tham số page và size truyền vào từ người dùng
        PageRequest pageable = PageRequest.of(page, size); // page là trang, size là số lượng trên mỗi trang

        // Lấy tất cả tài khoản doanh nghiệp (có phân trang)
        Page<AccountEnterprise> accountEnterprisesPage = accountEnterpriseRepository.findAll(pageable);

        // Chuyển đổi các tài khoản doanh nghiệp thành DTO (AccountEnterpriseResponse)
        return accountEnterprisesPage.getContent().stream()
                .map(accountEnterpriseMapper::toResponse) // Chuyển đổi sang DTO
                .collect(Collectors.toList());
    }



    @Override
    public AccountEnterpriseResponse getAccountEnterpriseById(Long id) {
        // Tìm tài khoản doanh nghiệp theo ID
        AccountEnterprise accountEnterprise = accountEnterpriseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound)); // Nếu không tìm thấy thì ném lỗi
        return accountEnterpriseMapper.toResponse(accountEnterprise); // Trả về response DTO
    }

    @Override
    public AccountEnterpriseResponse createAccountEnterprise(AccountEnterpriseDto accountEnterpriseDto) {
        validateForm(accountEnterpriseDto);
        // Chuyển đổi DTO thành entity và lưu vào repository
        AccountEnterprise accountEnterprise = accountEnterpriseMapper.toEntity(accountEnterpriseDto);

        accountEnterprise.setPassword(passwordEncoder.encode(accountEnterpriseDto.getPassword()));
        accountEnterprise.setStatus(false);
        accountEnterprise.setRole(roleRepository.findById(3L).orElseThrow(() -> new RuntimeException("Không tìm thấy role")));
        accountEnterprise = accountEnterpriseRepository.save(accountEnterprise);
        return accountEnterpriseMapper.toResponse(accountEnterprise); // Trả về response DTO
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

        if (accountEnterpriseDto.getPassword() != null && !accountEnterpriseDto.getPassword().isEmpty()) {
            accountEnterprise.setPassword(this.passwordEncoder.encode(accountEnterpriseDto.getPassword()));
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
                () -> new AppException(ErrorType.notFound) // Ném exception nếu không tìm thấy
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

    private void validateForm(AccountEnterpriseDto accountEnterpriseDto) {
        if (accountEnterpriseDto.getUsername() == null || accountEnterpriseDto.getUsername().isEmpty()) {
            throw new RuntimeException("Username không được để trống.");
        } else if (accountEnterpriseDto.getPassword() == null || accountEnterpriseDto.getPassword().isEmpty()) {
            throw new RuntimeException("Mật khẩu không được để trống.");
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
        } else if (Utils.isValidPhoneNumber(accountEnterpriseDto.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại không hợp lệ.");
        }
        if (accountEnterpriseRepository.findByUsername(accountEnterpriseDto.getUsername()) != null) {
            throw new AppException(ErrorType.usernameExisted);
        }
    }
}
