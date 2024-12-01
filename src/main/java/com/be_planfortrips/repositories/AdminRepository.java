package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("SELECT a FROM Admin a WHERE a.userName = :username AND a.password = :password")
    Admin findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Query("SELECT a FROM Admin a WHERE a.userName = :username")
    Admin findByUsername(@Param("username") String userName);

    @Query("Select Count(*) from Admin")
    Integer countAll();
}
