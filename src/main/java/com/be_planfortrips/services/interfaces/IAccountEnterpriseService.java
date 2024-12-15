package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.response.AccountEnterpriseResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAccountEnterpriseService {

    // Lấy tất cả tài khoản doanh nghiệp
    Page<AccountEnterpriseResponse> getAllAccountEnterprises(String name,int page, int size);

    // Lấy tài khoản doanh nghiệp theo ID
    AccountEnterpriseResponse getAccountEnterpriseById(Long id);

    // Tạo tài khoản doanh nghiệp mới
    AccountEnterpriseResponse createAccountEnterprise(AccountEnterpriseDto accountEnterpriseDto);

    // Cập nhật thông tin tài khoản doanh nghiệp
    AccountEnterpriseResponse updateAccountEnterprise(AccountEnterpriseDto accountEnterpriseDto);

    // Xóa tài khoản doanh nghiệp theo ID
    void deleteAccountEnterpriseById(Long id);

    // Lấy thông tin chi tiết tài khoản doanh nghiệp
    AccountEnterpriseResponse getAccountEnterpriseDetail();

    Boolean toggleStage(Long userId);

    void validateUsername(String username);

    void validateEmail(String email);

    void validatePhone(String phone);

    List<AccountEnterpriseResponse> getAccountEnterpriseDisable();

    void resetPassword(Integer id, String email, String phone);

    boolean validateContact(Integer serviceType, String email, String phone);

    List<AccountEnterpriseResponse> getAccountEnterpriseNeedAccept();

    AccountEnterpriseResponse getAccountEnterpriseByPhoneNumber(String phoneNumber);

    AccountEnterpriseResponse getAccountEnterpriseByEmail(String email);

    void uploadImage(MultipartFile file);

    void verifyPassword(String password);

}
