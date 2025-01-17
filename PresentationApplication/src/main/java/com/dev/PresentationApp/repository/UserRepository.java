package com.dev.PresentationApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.PresentationApp.entity.User;
import com.dev.PresentationApp.enums.Role;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	Optional<User> findById(Integer id);

	List<User> findAllUserByRole(Role role);

}
