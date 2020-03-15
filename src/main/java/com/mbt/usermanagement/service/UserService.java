package com.mbt.usermanagement.service;

import com.mbt.usermanagement.beans.User;

import java.util.List;

public interface UserService 
{
	public User getUser(Integer id);
	public User addUser(User user);
	public User updateUser(User user);
	public void deleteUser(Integer id);
	public List<User> getAllUsers();
	public User findUserByEmail(String email);
	public boolean authenticate(String email, String password);
	public User findUserByEmailAndPassword(String email, String password);
}
