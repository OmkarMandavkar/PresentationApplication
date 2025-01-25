package com.dev.PresentationApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.dev.PresentationApp.dto.UserRequest;
import com.dev.PresentationApp.entity.User;
import com.dev.PresentationApp.enums.Role;
import com.dev.PresentationApp.enums.Status;
import com.dev.PresentationApp.exception.UserNotFoundException;
import com.dev.PresentationApp.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean userRegister(UserRequest request) {
		Optional<User> opt = userRepository.findByEmail(request.getEmail());

		if (opt.isPresent()) {
			return false;
		} else {
			User user = new User();
			BeanUtils.copyProperties(request, user);
			userRepository.save(user);
			return true;
		}
	}

	public boolean userLogin(String email, String password) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User is not register"));
		if (user.getPassword().equals(password)) {
			user.setStatus(Status.ACTIVE);
			userRepository.save(user);
			return true;
		}
		return false;
	}

	public User updateUser(Integer id, UserRequest userRequest) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User Not found/Enter registered id"));
		if (userRequest.getName() != null)
			user.setName(userRequest.getName());
		if (userRequest.getEmail() != null)
			user.setEmail(userRequest.getEmail());
		if (userRequest.getPhone() != null)
			user.setPhone(userRequest.getPhone());
		if (userRequest.getPassword() != null)
			user.setPassword(userRequest.getPassword());
		return userRepository.save(user);
	}

	public Optional<User> getByUserId(Integer id) {
		return userRepository.findById(id);
	}

//	public List<User> getAllUsersByAdmin(Role role) {
//		return userRepository.findAllUserByRole(role);
//	}

	public List<User> getStudentsIfAdmin(Integer id) {

		User checkUser = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User Not found/Enter registered id"));

		Optional<User> optionalUser = userRepository.findById(id);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			if (user.getRole() == Role.ADMIN) {
				List<User> allUsers = userRepository.findAll();
				List<User> studentUsers = new ArrayList<>();
				for (User u : allUsers) {
					if (u.getRole() == Role.STUDENT) {
						studentUsers.add(u);
					}
				}
				return studentUsers;
			}
		}
		return new ArrayList<>();
	}

	public String updateStatusByUserId(Integer id, Status status) {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No User Found"));

		user.setStatus(status);
		userRepository.save(user);
		return "Status is updated to " + status;
	}
}
