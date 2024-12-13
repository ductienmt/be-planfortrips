package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.response.StatisticalCountYear;
import com.be_planfortrips.entity.AccountEnterprise;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

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

    @Query(value = "SELECT * FROM account_enterprises e WHERE unaccent(e.enterprise_name) LIKE unaccent(CONCAT('%', :name, '%'))", nativeQuery = true)
    Page<AccountEnterprise> findByEnterpriseNameStartingWithIgnoreCase(@Param("name") String name, Pageable pageable);

    Optional<AccountEnterprise> getAccountEnterpriseByPhoneNumber(String phoneNumber);

    @Query(value = "WITH months AS (\n" +
            "    SELECT 1 AS month\n" +
            "    UNION ALL SELECT 2\n" +
            "    UNION ALL SELECT 3\n" +
            "    UNION ALL SELECT 4\n" +
            "    UNION ALL SELECT 5\n" +
            "    UNION ALL SELECT 6\n" +
            "    UNION ALL SELECT 7\n" +
            "    UNION ALL SELECT 8\n" +
            "    UNION ALL SELECT 9\n" +
            "    UNION ALL SELECT 10\n" +
            "    UNION ALL SELECT 11\n" +
            "    UNION ALL SELECT 12\n" +
            ")\n" +
            "SELECT m.month,\n" +
            "       COUNT(ae.id) AS account_count\n" +
            "FROM months m\n" +
            "         LEFT JOIN account_enterprises ae ON EXTRACT(MONTH FROM ae.create_at) = m.month\n" +
            "    AND EXTRACT(YEAR FROM ae.create_at) = :year\n" +
            "GROUP BY m.month\n" +
            "ORDER BY m.month;\n", nativeQuery = true)
    List<StatisticalCountYear> StatisticalCountEtpByYear(Integer year);







}
