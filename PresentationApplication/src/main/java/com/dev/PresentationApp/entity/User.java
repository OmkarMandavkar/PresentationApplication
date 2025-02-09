package com.dev.PresentationApp.entity;

import com.dev.PresentationApp.enums.Role;
import com.dev.PresentationApp.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private Long phone;

    private String password;

    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Double userTotalScore;
}