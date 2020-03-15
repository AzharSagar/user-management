package com.mbt.usermanagement.repository;

import com.mbt.usermanagement.beans.Designation;
import com.mbt.usermanagement.beans.PermissionBean;
import com.mbt.usermanagement.beans.PermissionList;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.StringReader;
import java.util.List;

@Repository
public interface PermissionListRepo extends JpaRepository<PermissionList, Integer> {

    @Query(value = "SELECT * FROM assigned_permissions", nativeQuery = true)
    List<PermissionList> getPermissionList();

    @Query(value = "SELECT * FROM assigned_permissions where type =:type AND url =:url", nativeQuery = true)
    PermissionList getByTypeAndUrl(String type, String url);

    @Query(value = "SELECT description FROM assigned_permissions where url like CONCAT('%',:url,'%') AND  type = :type", nativeQuery = true)
    public String getDescription(String url , String type);




}
