package com.be_planfortrips.repositories;

import com.be_planfortrips.dto.sql.StatisticalCount;
import com.be_planfortrips.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT a FROM User a WHERE a.id = :id AND a.isActive= true")
    User findByIdActive(@Param("id") Long id);

    @Query("SELECT a FROM User a WHERE a.userName = :username AND a.isActive= true")
    User findByUsername(@Param("username") String username);

    @Query("SELECT a FROM User a WHERE a.email = :email AND a.isActive= true")
    User findByEmail(@Param("email") String email);

    @Query("SELECT a FROM User a WHERE a.userName = :username AND a.password = :password AND a.isActive= true")
    User findByUserNameAndPassword(@Param("username") String userName, @Param("password") String password);

    Optional<User> findUSerByGoogleId(String googleId);

    @Query(value = "SELECT i.url AS image_url " +
            "FROM users u LEFT JOIN images i ON u.image_id = i.id " +
            "WHERE u.id = :userId", nativeQuery = true)
    String getAvatarUser(@Param("userId") Long userId);

    @Query("select distinct u from Ticket t \n" +
            "inner join User u ON u.id = t.user.id \n" +
            "inner join Schedule s on t.schedule.id = s.id\n" +
            "inner join Vehicle v on v.code = s.vehicleCode.code\n" +
            "inner join CarCompany c on c.id = v.carCompany.id \n" +
            "where c.id = :car_company_id")
    List<User> findUserByCarCompanyId(@Param("car_company_id") Integer id);

    @Query("select distinct u from BookingHotel bh \n" +
            "inner join User u on u.id = bh.user.id \n" +
            "inner join BookingHotelDetail bhd ON bhd.bookingHotel.bookingHotelId = bh.bookingHotelId\n" +
            "inner join Room r ON r.id = bhd.room.id\n" +
            "inner join Hotel h ON h.id = r.hotel.id\n" +
            "where h.id = :hotel_id")
    List<User> findUserByHotelId(@Param("hotel_id") Long id);

    @Query("Select Count(*) from User")
    Integer countAll();

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
        COUNT(u.id) AS count  -- Thay đổi từ 'account_count' thành 'count'
    FROM months m 
    LEFT JOIN users u 
        ON EXTRACT(MONTH FROM u.create_at) = m.month 
        AND EXTRACT(YEAR FROM u.create_at) = :year 
    GROUP BY m.month 
    ORDER BY m.month;
""", nativeQuery = true)
    List<StatisticalCount> countUsersByYear(@Param("year") int year);

}


