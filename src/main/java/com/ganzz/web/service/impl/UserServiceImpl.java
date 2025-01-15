package com.ganzz.web.service.impl;

import com.ganzz.web.dto.RegistrationDto;
import com.ganzz.web.models.Role;
import com.ganzz.web.models.UserEntity;
import com.ganzz.web.repository.RoleRepository;
import com.ganzz.web.repository.UserRepository;
import com.ganzz.web.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;


@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registrationDto.getUsername());
        userEntity.setUsername(registrationDto.getEmail());
        userEntity.setPassword(registrationDto.getPassword());
        Role role = roleRepository.findByName("USER");
        userEntity.setRoles(Collections.singletonList(role));
        userRepository.save(userEntity);
    }
}
