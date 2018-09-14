package org.registration.service;

import org.registration.persistence.ParticipantEntity;

public interface EmailManager {

	public String sendConfirmationEmail(ParticipantEntity participant);
	
}
