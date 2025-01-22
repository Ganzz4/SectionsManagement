package com.ganzz.web.service;

import com.ganzz.web.dto.RegistrationDto;
import com.ganzz.web.model.UserEntity;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);
}
