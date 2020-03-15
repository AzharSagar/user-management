package com.mbt.usermanagement.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "permissionlist")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PermissionList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "permission_list_id")
	private Integer id;

	@Enumerated(EnumType.STRING)
	private ApiType type;

	private String url;
	private String description;

	/*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "permission")
	private List<PermissionList> permitList = new ArrayList<>();
*/
	//setters and getters

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ApiType getType() {
		return type;
	}

	public void setType(ApiType type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


}
