package org.registration.service;

import org.registration.persistence.ParticipantEntity;

public interface EmailManager {

	public void sendConfirmationEmail(ParticipantEntity participant);
	
}
