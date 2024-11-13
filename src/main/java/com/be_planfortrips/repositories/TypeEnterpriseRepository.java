package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.TypeEnterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TypeEnterpriseRepository extends JpaRepository<TypeEnterprise, Long> {
    Boolean existsByNameType(String nameType);

    @Query(value = """
    select case when count(te) > 0 then true else false end
    from type_enterprises te 
    join type_enterprise_details ted on te.id = ted.id
    join account_enterprises ae on ted.id = ae.type_detail_id
    where ted.id = :id
    """, nativeQuery = true)
    Boolean checkTypeEnterpriseHasEnterprise(@Param("id") Long id);


}
