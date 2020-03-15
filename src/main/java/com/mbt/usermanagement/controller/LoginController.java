package com.mbt.usermanagement.controller;


import com.mbt.usermanagement.beans.AssignedResponse;
import com.mbt.usermanagement.beans.MinimalJWTUser;
import com.mbt.usermanagement.beans.PermissionBean;
import com.mbt.usermanagement.beans.User;
import com.mbt.usermanagement.repository.DesignationRepository;
import com.mbt.usermanagement.repository.PermissionRepo;
import com.mbt.usermanagement.repository.UserRepository;
import com.mbt.usermanagement.service.JWTService;
import com.mbt.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class LoginController {

	@Autowired
	private JWTService jwtService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PermissionRepo permissionRepo;

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping(value = "/api/login")
	public ResponseEntity<?> auth(@RequestBody User user, HttpServletResponse response) {
		String email = user.getEmail();
		String password = user.getPassword();
		User userBean = userService.findUserByEmail(email);
		if (userBean != null) {
			if (userBean.getPassword()==null) {
				return new ResponseEntity<>("The Password Of User is Null", HttpStatus.BAD_REQUEST);
			}
			if(passwordEncoder.matches(password, userBean.getPassword())) {
				MinimalJWTUser jwtUser = new MinimalJWTUser(user.getEmail(), user.getDesignationName());
				HashMap<String, String> h = new HashMap<>();
				h.put("Authorization",jwtService.getToken(jwtUser));
				userBean.setLogin(true);
				userService.updateUser(userBean);
				return ResponseEntity.ok(h);
			} else {
				return new ResponseEntity<String>("Wrong Password", HttpStatus.UNAUTHORIZED);
			}
		}
		return new ResponseEntity<String>("Wrong Credentials", HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/api/assign/{userId}")
	public ResponseEntity<List<AssignedResponse>> getAssignedPermissions(@PathVariable Integer userId){
		User user = userRepository.getOne(userId);
		List<PermissionBean> permissions = permissionRepo.getPermissionByDesignation(designationRepository.findById(user.getDesignation().getId()).get().getId());
		List<AssignedResponse> list = new ArrayList<>();
		permissions.forEach(i->{
			AssignedResponse assignedResponse = new AssignedResponse();
			assignedResponse.setId(i.getPermitList().getId());
			assignedResponse.setDesignation_name(i.getDesignation().getDesignation());
			assignedResponse.setPermission_name(i.getPermitList().getDescription());
			list.add(assignedResponse);
		});
		return ResponseEntity.ok(list);
	}

	@PostMapping(value = "/api/logout")
	public ResponseEntity<?> logout(@RequestBody User user){
		if(userRepository.findByEmail(user.getEmail())!=null){
			User user1 = userRepository.findByEmail(user.getEmail());
			user1.setLogin(false);
			userService.addUser(user1);
		}
		return ResponseEntity.ok().build();
	}
}
