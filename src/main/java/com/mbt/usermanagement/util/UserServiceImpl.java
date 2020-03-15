package com.mbt.usermanagement.util;

import com.mbt.usermanagement.beans.User;
import com.mbt.usermanagement.repository.UserRepository;
import com.mbt.usermanagement.service.PasswordService;
import com.mbt.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.List;
 
@Service
public class UserServiceImpl implements UserService
{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private  PasswordService passwordService;
	
	
	public UserServiceImpl() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
	
	@Override
	public User getUser(Integer id) {
		if(userRepository.findById(id).isPresent()) {
			return userRepository.getUser(id);
		}
		else return null;
	}

	@Override
	public User addUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
		return userRepository.saveAndFlush(user);
	}

	@Override
	public void deleteUser(Integer id) {
		if(userRepository.findById(id).isPresent()) {
			userRepository.deleteUser(id);
		}
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.getAllUsers();
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public boolean authenticate(String email, String password) {
		User user = findUserByEmail(email);
//		this.passwordService.equals(password, user.getPassword());
	    return true;
	}

	@Override
	public User findUserByEmailAndPassword(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}
}
