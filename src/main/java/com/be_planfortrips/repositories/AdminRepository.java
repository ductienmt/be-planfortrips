package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query ("SELECT a FROM Admin a WHERE a.userName = :username")
    Admin findByUsername(@Param("username") String username);
}
