package com.mbt.usermanagement.repository;

import com.mbt.usermanagement.beans.Department;
import com.mbt.usermanagement.beans.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {

    public List<Designation> findAllByOrderByIdDesc();

   @Query(value = "SELECT * from designation where designation = :designation", nativeQuery = true)
   public Designation findByDesignation(String designation);

    @Query(value = "SELECT * from designation where department_id = :id", nativeQuery = true)
   public List<Designation> getDesignationsByDepartmentId(Integer id);

    @Query(value = "DELETE FROM designation WHERE designation_id = :id AND department_id = :did", nativeQuery = true)
   public Designation getDesignationByDepartmentId(Integer id, Integer did);

    public List<Designation> findByDepartment(Department id);




}
