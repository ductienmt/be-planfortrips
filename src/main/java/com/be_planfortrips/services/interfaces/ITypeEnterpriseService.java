package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.entity.TypeEnterprise;

import java.util.List;

public interface ITypeEnterpriseService {

    // Lấy tất cả loại doanh nghiệp
    List<TypeEnterprise> getAllTypeEnterprises();

    // Lấy loại doanh nghiệp theo ID
    TypeEnterprise getTypeEnterpriseById(Long id);

    // Tạo loại doanh nghiệp mới
    TypeEnterprise createTypeEnterprise(TypeEnterprise typeEnterprise);

    // Cập nhật thông tin loại doanh nghiệp
    TypeEnterprise updateTypeEnterprise(Long id, TypeEnterprise typeEnterprise);

    // Xóa loại doanh nghiệp theo ID
    void deleteTypeEnterpriseById(Long id);
}
