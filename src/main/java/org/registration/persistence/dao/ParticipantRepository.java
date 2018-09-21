package org.registration.persistence.dao;

import java.util.List;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {

	public ParticipantEntity findByFirstNameAndMiddleNameAndLastNameAndConference(String firstName, String middleName, String lastName, ConferenceEntity ce);
	public ParticipantEntity findByEmailAndConference(String email, ConferenceEntity ce);	
	public List<ParticipantEntity> findByConference(ConferenceEntity ce);
}
