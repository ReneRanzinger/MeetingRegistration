package org.registration.service;

import java.util.List;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.FeeEntity;
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
		
	public void createParticipant(ParticipantEntity newParticipant) {
		repository.save(newParticipant);
	}

	@Override
	public List<ParticipantEntity> findAllParticipantsByConference(ConferenceEntity ce) {
		
		return repository.findByConference(ce);
	}

	@Override
	public void deleteParticipantById(Long participantId) {
		
		repository.deleteById(participantId);
		
	}

	@Override
	public ParticipantEntity findByParticipantId(Long participantId) {
		
		return repository.findByParticipantId(participantId);
	}

	@Override
	public ParticipantEntity findByParticipantIdAndEmail(Long participantId, String email) {
		
		return repository.findByParticipantIdAndEmail(participantId, email);
	}

	@Override
	public List<ParticipantEntity> findAllParticipantsByFee(FeeEntity fe) {
		
		return repository.findByFee(fe);
	}
	
	

}
