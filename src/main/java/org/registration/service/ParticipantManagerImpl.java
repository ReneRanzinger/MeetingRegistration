package org.registration.service;

import org.registration.persistence.ParticipantEntity;
import org.registration.persistence.dao.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value="jpaTransactionManager")
public class ParticipantManagerImpl implements ParticipantManager {

	@Autowired
	ParticipantRepository repository;
	
	@Override
	public void addParticipant(ParticipantEntity newParticipant) {
		repository.save(newParticipant);
	}

}
