package com.mbt.usermanagement.repository;

import com.mbt.usermanagement.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
	@Query(value="SELECT * FROM user where active = 1 AND email = :email",nativeQuery = true)
	public User  findByEmail(String email);
	
	@Query(value="SELECT * FROM user where active = 1 AND email = :email AND password = :password",nativeQuery = true)
	public User findByEmailAndPassword(String email, String password);
	
	@Modifying
    @Transactional
	 @Query(value="UPDATE user set active = 0 where user_id = :id", nativeQuery = true)
	public void deleteUser(Integer id);
	
	@Query(value="SELECT * FROM user where active = 1", nativeQuery = true)
	public List<User> getAllUsers();
	
	@Query(value="SELECT * FROM user where active = 1 AND user_id = :id", nativeQuery = true)
	public User getUser(Integer id);

	@Modifying
	@Transactional
	@Query(value="UPDATE user set active = 1 where user_id = :id", nativeQuery = true)
	public void setUserEnable(Integer id);

	@Modifying
	@Transactional
	@Query(value="UPDATE user set active = 0 where user_id = :id", nativeQuery = true)
	public void setUserDisable(Integer id);

}
