package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.response.AccountEnterpriseResponse;

import java.util.List;

public interface IAccountEnterpriseService {

    // Lấy tất cả tài khoản doanh nghiệp
    List<AccountEnterpriseResponse> getAllAccountEnterprises();

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

    // Thay đổi trạng thái tài khoản doanh nghiệp (1 la active, 0 la inactive)
    void changeStatus(Long id, Integer status);
}
