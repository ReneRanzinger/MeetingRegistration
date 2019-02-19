package org.registration.persistence.dao;

import java.util.List;

import org.registration.persistence.ConferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceRepository extends JpaRepository<ConferenceEntity, Long> {

	public ConferenceEntity findByConferenceCode(String conferenceCode);	
	public List<ConferenceEntity> findAll();
	public ConferenceEntity findByConferenceId(Long conferenceId);
		
}
