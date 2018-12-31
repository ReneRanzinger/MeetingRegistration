package org.registration.service;

import java.util.List;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.ParticipantEntity;

public interface ParticipantManager {

	void createParticipant(ParticipantEntity newParticipant);
	List<ParticipantEntity> findAllParticipantsByConference(ConferenceEntity ce);
	void deleteParticipantById(Long participantId);
	ParticipantEntity findByParticipantId(Long participantId);
	ParticipantEntity findByParticipantIdAndEmail(Long participantId, String email);
}
