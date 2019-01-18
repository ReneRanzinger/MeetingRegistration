package org.registration.persistence.dao;

import org.registration.persistence.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, String>{

	public AdminEntity findByUsername(String username);
	
}
