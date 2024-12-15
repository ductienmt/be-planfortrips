package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.sql.StatisticalCount;
import com.be_planfortrips.dto.sql.StatisticalCountMonth;
import com.be_planfortrips.entity.AccountEnterprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Query("Select ae From AccountEnterprise ae WHERE ae.status = false and ae.createAt = ae.updateAt")
    List<AccountEnterprise> findAccountEnterpriseNeedAccept();

    @Query("Select count(*) from AccountEnterprise")
    Integer countAll();

    @Query("SELECT a FROM AccountEnterprise a WHERE a.typeEnterpriseDetail.id = :id AND a.email = :email AND a.phoneNumber = :phone")
    Optional<AccountEnterprise> findByServiceTypeAndEmailAndPhone(@Param("id") Integer id,
                                                                  @Param("email") String email,
                                                                  @Param("phone") String phone);

    @Query("SELECT a FROM AccountEnterprise a WHERE a.typeEnterpriseDetail.id = :serviceType AND a.status = true")
    List<AccountEnterprise> findActiveByServiceType(@Param("serviceType") Integer serviceType);
    Page<AccountEnterprise> findByEnterpriseNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<AccountEnterprise> getAccountEnterpriseByPhoneNumber(String phoneNumber);

    Optional<AccountEnterprise> getAccountEnterpriseByEmail(String email);

    @Query(value = """
    WITH months AS (
        SELECT 1 AS month
        UNION ALL SELECT 2
        UNION ALL SELECT 3
        UNION ALL SELECT 4
        UNION ALL SELECT 5
        UNION ALL SELECT 6
        UNION ALL SELECT 7
        UNION ALL SELECT 8
        UNION ALL SELECT 9
        UNION ALL SELECT 10
        UNION ALL SELECT 11
        UNION ALL SELECT 12
    )
    SELECT 
        m.month AS date,  -- Thay đổi từ 'month' thành 'date'
        COUNT(ae.id) AS count  -- Thay đổi từ 'account_count' thành 'count'
    FROM months m
    LEFT JOIN account_enterprises ae ON EXTRACT(MONTH FROM ae.create_at) = m.month
        AND EXTRACT(YEAR FROM ae.create_at) = :year
    GROUP BY m.month
    ORDER BY m.month;
""", nativeQuery = true)
    List<StatisticalCount> StatisticalCountEtpByYear(@Param("year") Integer year);





    @Query(value = """
        WITH days AS (
            SELECT generate_series(1, 31) AS day
        ),
        daily_stats AS (
            SELECT
                EXTRACT(DAY FROM ae.create_at) AS day,
                COUNT(ae.id) AS account_count
            FROM account_enterprises ae
            WHERE EXTRACT(YEAR FROM ae.create_at) = :year
              AND EXTRACT(MONTH FROM ae.create_at) = :month
            GROUP BY EXTRACT(DAY FROM ae.create_at)
        )
        SELECT d.day AS date,  -- Thay 'd.day' thành 'date'
               COALESCE(ds.account_count, 0) AS count  -- Thay 'ds.account_count' thành 'count'
        FROM days d
        LEFT JOIN daily_stats ds ON d.day = ds.day
        ORDER BY d.day;
        """, nativeQuery = true)
    List<StatisticalCount> StatisticalCountEtpByMonth(
            @Param("year") int year, @Param("month") int month);





}
