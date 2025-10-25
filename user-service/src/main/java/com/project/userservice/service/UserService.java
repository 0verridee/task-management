package com.project.userservice.service;


import com.project.userservice.entity.User;
import com.project.userservice.entity.UserStatus;
import com.project.userservice.repository.UserRepository;
import com.project.userservice.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;


    // find user by id
    public User findUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id= " + id + ", not found!"));

        return mapper.toDomainEntity(user);

    }

    // find all users
    public List<User> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    // create user
    public User createUser(User userToCreate) {
        var userEntityToSave = mapper.toEntity(userToCreate);

        var savedUser = userRepository.save(userEntityToSave);

        return mapper.toDomainEntity(savedUser);
    }

    // update user
    public User updateUser(Long id, User userToUpdate) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id= " + id + ", not found!"));

        if(user.getUserStatus().equals(UserStatus.BANNED)){
            throw new IllegalStateException("Cannot modify user! status= " + user.getUserStatus());
        }

        if(user.getUserStatus().equals(UserStatus.DELETED)){
            throw new IllegalStateException("Cannot modify user! status= " + user.getUserStatus());
        }

        user.setUsername(userToUpdate.username());
        user.setEmail(userToUpdate.email());


        var updatedTask = userRepository.save(user);
        return mapper.toDomainEntity(updatedTask);
    }

    // change status to DELETED
    @Transactional
    public void deleteUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id= " + id + ", not found!"));

        if(user.getUserStatus().equals(UserStatus.DELETED)){
            throw new IllegalStateException("Cannot delete user! status= " + user.getUserStatus());
        }

        user.setUserStatus(UserStatus.BANNED);
        userRepository.save(user);

    }

    // change status to BANNED
    @Transactional
    public void banUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id= " + id + ", not found!"));

        if(user.getUserStatus().equals(UserStatus.BANNED)){
            throw new IllegalStateException("Cannot delete user! status= " + user.getUserStatus());
        }

        if(user.getUserStatus().equals(UserStatus.DELETED)){
            throw new IllegalStateException("Cannot delete user! status= " + user.getUserStatus());
        }

        user.setUserStatus(UserStatus.BANNED);
        userRepository.save(user);
    }


}
