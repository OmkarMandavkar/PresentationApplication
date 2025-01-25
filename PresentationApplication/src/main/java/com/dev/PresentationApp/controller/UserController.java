package com.dev.PresentationApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.PresentationApp.dto.UserRequest;
import com.dev.PresentationApp.dto.UserResponse;
import com.dev.PresentationApp.entity.User;
import com.dev.PresentationApp.enums.Status;
import com.dev.PresentationApp.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/*
	 * Used to Register User (ADMIN or STUDENT)
	 * @RequestBody userRequest - JSON
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
	 * Used to login user
	 * @Param : email, @Param : password
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
	 * Used to update user details
	 * @Param : id
	 */
	@PutMapping(value = "/update", consumes = { "application/json", "application/xml" }, produces = {
			"application/json", "application/xml" })
	public ResponseEntity<UserResponse> updateCounsellor(@RequestParam Integer id,
			@RequestBody UserRequest userRequest) {
		User updateUser = userService.updateUser(id, userRequest);

		UserResponse ur = new UserResponse();
		BeanUtils.copyProperties(updateUser, ur);
		return new ResponseEntity<UserResponse>(ur, HttpStatus.OK);
	}

	/*
	 * Used to fetch user details by id
	 * @Param id
	 */
	@GetMapping("/getUserById")
	public ResponseEntity<?> getUserById(@RequestParam Integer id) {
		Optional<User> userData = userService.getByUserId(id);
		if (userData.isPresent()) {
			return new ResponseEntity<>(userData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User Not Found", HttpStatus.NOT_FOUND);
		}
	}

	/*
	 * Used to fetch all the record of student if and only if the user is ADMIN
	 * @Param id 
	 */
	@GetMapping("/admin/getAllUserByAdmin")
    public ResponseEntity<?> getStudentsIfAdmin(@RequestParam Integer id) {
        List<User> studentUsers = userService.getStudentsIfAdmin(id);
        if (studentUsers.isEmpty()) {
            return ResponseEntity.status(403).body("Access denied. User is not an ADMIN or no students found.");
        }
        return ResponseEntity.ok(studentUsers);
    }
	
	/*
	 * Used to update User status by ADMIN
	 * @Param id 
	 */	
	@PatchMapping("/updateUserStatus")
	public ResponseEntity<String> updateUserStatus(@RequestParam Integer id, @RequestParam Status status) {
		String updateStatus = userService.updateStatusByUserId(id, status);
		return new ResponseEntity<String>(updateStatus, HttpStatus.OK);
	}
}