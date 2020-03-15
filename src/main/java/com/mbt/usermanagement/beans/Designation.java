package com.mbt.usermanagement.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "designation")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Designation
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "designation_id")
	private Integer id;

	@Column(name = "designation")
	private String designation;

	@ManyToOne
	private Department department;

	private String departmentName;

	private Date createdAt = new Date();
	private Boolean active = true;

	@Transient
	private Integer departmentId;

	//setters and getters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}



}
