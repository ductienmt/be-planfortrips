package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.TypeEnterpriseDetailDto;
import com.be_planfortrips.dto.response.AccountEnterpriseResponse;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.AccountEnterpriseMapper;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.RoleRepository;
import com.be_planfortrips.services.interfaces.IAccountEnterpriseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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
        if (accountEnterpriseRepository.findByUsername(accountEnterpriseDto.getUsername()) != null) {
            throw new AppException(ErrorType.usernameExisted);
        }
        // Chuyển đổi DTO thành entity và lưu vào repository
        AccountEnterprise accountEnterprise = accountEnterpriseMapper.toEntity(accountEnterpriseDto);

        accountEnterprise.setPassword(passwordEncoder.encode(accountEnterpriseDto.getPassword()));
        accountEnterprise.setRole(roleRepository.findById(3L).orElseThrow(() -> new RuntimeException("Không tìm thấy role với id: 2")));
        accountEnterprise = accountEnterpriseRepository.save(accountEnterprise);
        return accountEnterpriseMapper.toResponse(accountEnterprise); // Trả về response DTO
    }

    @Override
    public AccountEnterpriseResponse updateAccountEnterprise(Long id, AccountEnterpriseDto accountEnterpriseDto) {
        // Tìm tài khoản doanh nghiệp theo ID
        AccountEnterprise accountEnterprise = accountEnterpriseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound));
        // Cập nhật thông tin từ DTO
        AccountEnterprise aEtpNew = accountEnterpriseMapper.toEntity(accountEnterpriseDto);
        aEtpNew.setAccountEnterpriseId(accountEnterprise.getAccountEnterpriseId());
        accountEnterprise.setPassword(passwordEncoder.encode(accountEnterpriseDto.getPassword()));
        System.out.println(accountEnterprise.getCity().getNameCity());
        // Lưu entity đã cập nhật
        accountEnterpriseRepository.save(aEtpNew);

        // Trả về DTO sau khi cập nhật
        return accountEnterpriseMapper.toResponse(aEtpNew);
    }

    @Override
    public void deleteAccountEnterpriseById(Long id) {
        // Tìm tài khoản doanh nghiệp theo ID
        AccountEnterprise accountEnterprise = accountEnterpriseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound)); // Nếu không tìm thấy thì ném lỗi

        // Xóa tài khoản doanh nghiệp
        accountEnterpriseRepository.delete(accountEnterprise);
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

}
