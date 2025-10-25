package com.project.userservice.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record User(

        @Null
        Long id,

        @NotNull
        String username,

        @NotNull
        String email,
        LocalDateTime registrationDate,
        UserStatus userStatus
) {
}
