package com.mbt.usermanagement.repository;

import com.mbt.usermanagement.beans.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    public List<Department> findAllByOrderByIdDesc();



}
