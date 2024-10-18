package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT a FROM User a WHERE a.id = :id AND a.isActive= true")
    User findByIdActive(@Param("id") Long id);

    @Query("SELECT a FROM User a WHERE a.userName = :username AND a.isActive= true")
    User findByUsername(@Param("username") String username);

    @Query("SELECT a FROM User a WHERE a.email = :email AND a.isActive= true")
    User findByEmail(@Param("email") String email);
}
