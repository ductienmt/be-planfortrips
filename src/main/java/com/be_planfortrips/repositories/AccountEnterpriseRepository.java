package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.response.AccountEnterpriseResponse;
import com.be_planfortrips.entity.AccountEnterprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface AccountEnterpriseRepository extends JpaRepository<AccountEnterprise, Long> {
    @Query("SELECT a FROM AccountEnterprise a WHERE a.username = :username AND a.password = :password AND a.status = true")
    AccountEnterprise findByUsernameAndPassword(String username, String password);

    @Query("SELECT a FROM AccountEnterprise a WHERE a.username = :username AND a.status = true")
    AccountEnterprise findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phone);

    @Query("SELECT ae FROM AccountEnterprise ae WHERE ae.status = false")
    List<AccountEnterprise> findAccountEnterpriseDisable();

    @Query("Select count(*) from AccountEnterprise")
    Integer countAll();




}
