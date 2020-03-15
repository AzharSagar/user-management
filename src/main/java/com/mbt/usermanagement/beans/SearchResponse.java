package com.mbt.usermanagement.beans;

public class SearchResponse {
	Integer Id;
	String Name;

	public SearchResponse(Integer id, String name) {
		Id = id;
		Name = name;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

}
