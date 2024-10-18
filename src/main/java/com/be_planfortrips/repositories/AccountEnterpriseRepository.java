package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.AccountEnterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountEnterpriseRepository extends JpaRepository<AccountEnterprise, Long> {
    @Query("SELECT a FROM AccountEnterprise a WHERE a.username = :username")
    AccountEnterprise findByUsername(@Param("username") String username);
}
