package org.registration.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="configuration", schema= "registration")
public class ConfigurationEntity {

	public ConfigurationEntity() {
		
	}
	
	public ConfigurationEntity(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	@Column(nullable=false, unique=true, length=255)
	@Id
	String name;
	
	@Column(nullable=false, length=255)
	String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
