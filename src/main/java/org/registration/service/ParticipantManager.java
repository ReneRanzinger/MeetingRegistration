package org.registration.service;

import org.registration.persistence.ParticipantEntity;

public interface ParticipantManager {

	void createParticipant(ParticipantEntity newParticipant);
	
}
