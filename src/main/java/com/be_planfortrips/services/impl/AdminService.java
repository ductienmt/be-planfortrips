package com.be_planfortrips.services.impl;

import com.be_planfortrips.entity.Admin;
import com.be_planfortrips.repositories.AdminRepository;
import com.be_planfortrips.services.interfaces.IAdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AdminService implements IAdminService {
    AdminRepository adminRepository;
    @Override
    public Admin findAdminByUsername(String userName) {
        return adminRepository.findByUsername(userName);
    }
}
