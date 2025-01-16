package com.ganzz.web.repository;

import com.ganzz.web.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByUsername(String username);
    UserEntity findUserByEmail(String email);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

    UserEntity findFirstByUsername(String username);
}
