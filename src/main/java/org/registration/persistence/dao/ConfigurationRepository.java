package org.registration.persistence.dao;

import org.registration.persistence.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, String> {

	public ConfigurationEntity findByName(String name);
	
}
