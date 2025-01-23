package com.dev.PresentationApp.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.PresentationApp.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	Optional<User> findById(Integer id);

}
