package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.AccountEnterprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriseRepository extends JpaRepository<AccountEnterprise,Long> {
    AccountEnterprise findByEnterpriseName(String name);
}
