package org.registration.service;

import org.registration.persistence.ConferenceEntity;

public interface ConferenceManager {

	public void createConference(ConferenceEntity newConference);
	public ConferenceEntity findByConferenceCode(String conferenceCode);
}
