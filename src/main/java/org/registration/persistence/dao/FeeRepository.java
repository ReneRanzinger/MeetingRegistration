package org.registration.persistence.dao;

import java.util.List;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<FeeEntity, Long> {

	public FeeEntity findByNameAndConferenceEntity(String name, ConferenceEntity conference);
	public List<FeeEntity> findByConferenceEntity(ConferenceEntity conference);
}

