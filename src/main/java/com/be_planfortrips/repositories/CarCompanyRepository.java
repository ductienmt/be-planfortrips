package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.CarCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarCompanyRepository extends JpaRepository<CarCompany,Integer> {
}
