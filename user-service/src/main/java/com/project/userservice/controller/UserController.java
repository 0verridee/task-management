package com.project.userservice.controller;


import com.project.userservice.entity.User;
import com.project.userservice.repository.UserRepository;
import com.project.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable("id") Long id
    ) {
        log.info("Called findUserById(): id={}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        log.info("Called findAllUsers()");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findAllUsers());
    }

    @PostMapping()
    public ResponseEntity<User> createUser(
            @RequestBody @Valid User userToCreate
    ) {
        log.info("Called createUser()");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userToCreate));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ){
        log.info("Called deleteUser() id={}", id);

        userService.deleteUser(id);

        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PostMapping("/{id}/banUser")
    public ResponseEntity<Void> banUser(
            @PathVariable Long id
    ) {
        log.info("Called banUser(): id={}", id);

        userService.banUser(id);

        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id") Long id,
            @RequestBody User userToUpdate
    ) {
        log.info("Caleld updateUser(): id={}", id);

        var updatedUser = userService.updateUser(id,userToUpdate);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

}
