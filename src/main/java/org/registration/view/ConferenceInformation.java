package org.registration.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.registration.persistence.FeeEntity;

public class ConferenceInformation {

	private String conferenceCode;
	private String registrationStart;
	private String registrationEnd;
	private String abstractStart;
	private String abstractEnd;
	private List<FeeType> Fees;
	int statusCode;
	
	public ConferenceInformation(String conferenceCode) {
		super();
		this.conferenceCode = conferenceCode;
	}

	public ConferenceInformation() {
		// TODO Auto-generated constructor stub
	}

	public String getConferenceCode() {
		return conferenceCode;
	}

	public void setConferenceCode(String conferenceCode) {
		this.conferenceCode = conferenceCode;
	}

	public String getRegistrationStart() {
		return registrationStart;
	}

	public void setRegistrationStart(String registrationStart) {
		this.registrationStart = registrationStart;
	}

	public String getRegistrationEnd() {
		return registrationEnd;
	}

	public void setRegistrationEnd(String registrationEnd) {
		this.registrationEnd = registrationEnd;
	}

	public String getAbstractStart() {
		return abstractStart;
	}

	public void setAbstractStart(String abstractStart) {
		this.abstractStart = abstractStart;
	}

	public String getAbstractEnd() {
		return abstractEnd;
	}

	public void setAbstractEnd(String abstractEnd) {
		this.abstractEnd = abstractEnd;
	}

	public List<FeeType> getFees() {
		return Fees;
	}

	public void setFees(List<FeeType> fees) {
		Fees = fees;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
