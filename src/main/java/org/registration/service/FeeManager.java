package org.registration.service;

import java.util.List;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.FeeEntity;

public interface FeeManager {

	public void createFee(FeeEntity newFee);
	public FeeEntity findByNameAndConferenceEntity(String name, ConferenceEntity conference);
	public List<FeeEntity> findByConferenceCode(String conferenceCode);
}
