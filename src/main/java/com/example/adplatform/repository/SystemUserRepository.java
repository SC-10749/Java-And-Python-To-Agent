package com.example.adplatform.repository;

import com.example.adplatform.domain.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    Optional<SystemUser> findByUsername(String username);
}
