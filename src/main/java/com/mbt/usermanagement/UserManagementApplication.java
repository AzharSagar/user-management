package com.mbt.usermanagement;

import com.mbt.usermanagement.repository.DesignationRepository;
import com.mbt.usermanagement.repository.PermissionListRepo;
import com.mbt.usermanagement.repository.PermissionRepo;
import com.mbt.usermanagement.repository.UserRepository;
import com.mbt.usermanagement.util.PreRequists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserManagementApplication implements CommandLineRunner {

	@Autowired
	private PreRequists preRequists;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Autowired
	private PermissionRepo permissionRepo;

	@Autowired
	private PermissionListRepo permissionListRepo;


	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		if(designationRepository.findAll().size()==0){
			preRequists.createRole();
		}
		if(userRepository.findAll().size()==0){
			preRequists.createUser();
		}
		if(permissionListRepo.findAll().size()==0){
			preRequists.createPermissionList();
		}
		if(permissionRepo.findAll().size()==0){
			preRequists.createPermission();
		}
	}
}
