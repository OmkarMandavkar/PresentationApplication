package com.dev.PresentationApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.PresentationApp.dto.UserRequest;
import com.dev.PresentationApp.dto.UserResponse;
import com.dev.PresentationApp.entity.User;
import com.dev.PresentationApp.enums.Role;
import com.dev.PresentationApp.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/*
	 * USED TO REGISTER USER Input : JSON
	 */
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest) {

		boolean registered = userService.userRegister(userRequest);

		if (registered) {
			return new ResponseEntity<String>("User Registered Succefully", HttpStatus.CREATED);
		}
		return ResponseEntity.badRequest().body("Already Registered");
	}

	/*
	 * USED TO LOGIN USER
	 * 
	 * @Param : String email
	 * 
	 * @Param : String password
	 */
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
		boolean login = userService.userLogin(email, password);
		if (login) {
			return new ResponseEntity<String>("Login Successful", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Invalid Credentials", HttpStatus.BAD_REQUEST);
	}

	/*
	 * USED TO UPDATE USER DETAILS
	 * 
	 * @Param : Integer id
	 */
	@PutMapping(value = "/update/{id}", consumes = { "application/json", "application/xml" }, produces = {
			"application/json", "application/xml" })
	public ResponseEntity<UserResponse> updateCounsellor(@PathVariable Integer id,
			@RequestBody UserRequest userRequest) {
		User updateUser = userService.updateUser(id, userRequest);

		UserResponse ur = new UserResponse();
		BeanUtils.copyProperties(updateUser, ur);
		return new ResponseEntity<UserResponse>(ur, HttpStatus.OK);
	}

	/*
	 * USED TO DELETE USER DETAILS 
	 * @Param : Integer id
	 */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
		String deleteUser = userService.deleteUser(id);
		return new ResponseEntity<String>(deleteUser, HttpStatus.OK);
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<User> getByUserId(@PathVariable Integer id) {
		Optional<User> userData = userService.getByUserId(id);
		if (userData.isPresent()) {
			return new ResponseEntity<>(userData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getAllUserByAdmin")
	public ResponseEntity<List<User>> getAllUserByAdmin() {
		List<User> userData = userService.getAllUsersByAdmin(Role.STUDENT); 
		if (!userData.isEmpty()) {
			return new ResponseEntity<>(userData, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}