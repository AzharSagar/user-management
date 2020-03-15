package com.mbt.usermanagement.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Proxy(lazy = false)
@Entity
@Table(name = "assigned_permissions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PermissionBean  {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "permission_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "designation")
	private Designation designation;

	/*@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },
			fetch = FetchType.EAGER)
	@JoinTable(name = "PermissionList_PermissionBean",
			joinColumns = { @JoinColumn(name = "permission_bean_id") },
			inverseJoinColumns = { @JoinColumn(name = "permission_list_id") })*/
	@ManyToOne
	@JoinColumn(name = "permission")
	private PermissionList permitList;

	@Transient
	private Integer permissionListId;

	@Transient
	private List<Integer> permissionList = new ArrayList<>();

	@Transient
	private Integer designationId;


	//setter and getter


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public PermissionList getPermitList() {
		return permitList;
	}

	public void setPermitList(PermissionList permitList) {
		this.permitList = permitList;
	}

	public Integer getPermissionListId() {
		return permissionListId;
	}

	public void setPermissionListId(Integer permissionListId) {
		this.permissionListId = permissionListId;
	}

	public List<Integer> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Integer> permissionList) {
		this.permissionList = permissionList;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}
}
