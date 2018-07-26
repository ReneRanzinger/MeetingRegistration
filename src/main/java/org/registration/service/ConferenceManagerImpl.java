package org.registration.service;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.dao.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value="jpaTransactionManager")
public class ConferenceManagerImpl implements ConferenceManager {

	@Autowired
	ConferenceRepository repository;
	
	@Override
	public void createConference(ConferenceEntity newConference) {
		repository.save(newConference);		
	}

	@Override
	public ConferenceEntity findByConferenceCode(String conferenceCode) {
		
		return repository.findByConferenceCode(conferenceCode);
	}

}
