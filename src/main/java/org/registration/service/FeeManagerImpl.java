package org.registration.service;

import java.util.List;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.FeeEntity;
import org.registration.persistence.dao.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value="jpaTransactionManager")
public class FeeManagerImpl implements FeeManager {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	FeeRepository repository;
	
	
	@Override
	public void createFee(FeeEntity newFee) {
		repository.save(newFee);
	}

	@Override
	public List<FeeEntity> findByConferenceCode(String conferenceCode) {
		return repository.findByConferenceEntity(conferenceManager.findByConferenceCode(conferenceCode));
	}

	@Override
	public FeeEntity findByNameAndConferenceEntity(String name, ConferenceEntity conference) {
		
		return repository.findByNameAndConferenceEntity(name, conference);
	}

	
	
}
