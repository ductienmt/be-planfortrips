package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.TypeEnterpriseDetailDto;
import com.be_planfortrips.dto.response.AccountEnterpriseResponse;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.TokenMapper;
import com.be_planfortrips.mappers.impl.AccountEnterpriseMapper;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.RoleRepository;
import com.be_planfortrips.services.interfaces.IAccountEnterpriseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<AccountEnterpriseResponse> getAllAccountEnterprises() {
        // Lấy tất cả tài khoản doanh nghiệp từ repository
        List<AccountEnterprise> accountEnterprises = accountEnterpriseRepository.findAll();
        return accountEnterprises.stream()
                .map(accountEnterpriseMapper::toResponse) // Chuyển đổi sang DTO
                .toList();
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
        // Chuyển đổi DTO thành entity và lưu vào repository
        AccountEnterprise accountEnterprise = accountEnterpriseMapper.toEntity(accountEnterpriseDto);
        accountEnterprise.setPassword(passwordEncoder.encode(accountEnterpriseDto.getPassword()));
        accountEnterprise.setRole(roleRepository.findById(3L).orElseThrow(() -> new RuntimeException("Không tìm thấy role với id: 2")));
        accountEnterprise = accountEnterpriseRepository.save(accountEnterprise);
        return accountEnterpriseMapper.toResponse(accountEnterprise); // Trả về response DTO
    }

    @Override
    public AccountEnterpriseResponse updateAccountEnterprise(AccountEnterpriseDto accountEnterpriseDto) {
        // Tìm tài khoản doanh nghiệp theo ID
        AccountEnterprise accountEnterprise = accountEnterpriseRepository.findById(tokenMapper.getIdEnterpriseByToken())
                .orElseThrow(() -> new AppException(ErrorType.notFound));

        // Cập nhật thông tin từ DTO
        accountEnterpriseMapper.updateEntityFromDto(accountEnterpriseDto, accountEnterprise);
        accountEnterprise.setPassword(passwordEncoder.encode(accountEnterpriseDto.getPassword()));
        // Lưu entity đã cập nhật
        accountEnterprise = accountEnterpriseRepository.save(accountEnterprise);

        // Trả về DTO sau khi cập nhật
        return accountEnterpriseMapper.toResponse(accountEnterprise);
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
    public void changeStatus(Long id, Integer status) {
        AccountEnterprise accountEnterprise = accountEnterpriseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        if (status == 1) {
            accountEnterprise.setStatus(true);
        } else if (status == 0) {
            accountEnterprise.setStatus(false);
        } else {
            throw new AppException(ErrorType.statusInvalid);
        }
        accountEnterpriseRepository.save(accountEnterprise);
    }
}
