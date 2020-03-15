package com.mbt.usermanagement.repository;

import com.mbt.usermanagement.beans.PermissionBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("permissionRepository")
public interface PermissionRepo extends JpaRepository<PermissionBean, Integer> {

	@Query(value = "SELECT * FROM assigned_permissions where designation = :id", nativeQuery = true)
	public List<PermissionBean> getPermissionByDesignation(int id);

	@Query(value = "SELECT * FROM assigned_permissions where permission = :permissionId AND designation = :designationId", nativeQuery = true)
	public PermissionBean getPerticularPermissionOfDesignation(Integer permissionId, Integer designationId);


	@Query(value = "SELECT * From assigned_permissions where designation=:id", nativeQuery = true)
	public List<PermissionBean> getPermissonListByDesignationId(Integer id);
}
