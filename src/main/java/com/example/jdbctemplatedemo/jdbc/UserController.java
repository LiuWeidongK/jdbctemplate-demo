package com.example.jdbctemplatedemo.jdbc;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lwd
 * Created on 2023/10/17
 */
public class UserController {

    @Autowired
    UserRepository userRepository;

    public void main(UserEntity userEntity) {

        userRepository.update(userEntity);
    }
}
