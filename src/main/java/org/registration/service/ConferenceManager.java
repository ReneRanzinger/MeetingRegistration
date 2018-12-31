package org.registration.service;

import java.util.List;

import org.registration.persistence.ConferenceEntity;

public interface ConferenceManager {

	public void createConference(ConferenceEntity newConference);
	public ConferenceEntity findByConferenceCode(String conferenceCode);
	public List<ConferenceEntity> findAllConferences();
	public ConferenceEntity findByConferenceId(Long conferenceId);
}
