package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
