package com.project.userservice.utils;


import com.project.userservice.entity.User;
import com.project.userservice.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomainEntity(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRegistrationDate(),
                entity.getUserStatus()
        );
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.username(),
                user.email(),
                user.registrationDate(),
                user.userStatus()
        );
    }
}
