package com.mbt.usermanagement.beans;

/**
 * This class represents a logged JWT user's credentials.
 *
 */
public class MinimalJWTUser {

	private String email;
	private String role;

	public MinimalJWTUser() {
	}

	public MinimalJWTUser(String email, String role) {
		super();
		this.email = email;
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setUsername(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRoles(String role) {
		this.role = role;
	}
}
