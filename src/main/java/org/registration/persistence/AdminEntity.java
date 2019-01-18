package org.registration.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="admin", schema= "registration")
public class AdminEntity {

	public AdminEntity() {
		
	}
	
	public AdminEntity(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	
	@Column(nullable=false, length=255)
	@Id
	String username;
	
	@Column(nullable=false, length=255)
	String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
