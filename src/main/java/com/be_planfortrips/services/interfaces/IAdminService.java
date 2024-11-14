package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.entity.Admin;

public interface IAdminService {
    Admin findAdminByUsername(String userName);
}
