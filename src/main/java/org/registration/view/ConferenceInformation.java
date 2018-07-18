package org.registration.view;

import java.util.Date;
import java.util.HashMap;

public class ConferenceInformation {

	String conferenceCode;
	Date registrationStart;
	Date registrationEnd;
	Date abstractStart;
	Date abstractEnd;
	HashMap<String,Double> Fees;
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

	public Date getRegistrationStart() {
		return registrationStart;
	}

	public void setRegistrationStart(Date registrationStart) {
		this.registrationStart = registrationStart;
	}

	public Date getRegistrationEnd() {
		return registrationEnd;
	}

	public void setRegistrationEnd(Date registrationEnd) {
		this.registrationEnd = registrationEnd;
	}

	public Date getAbstractStart() {
		return abstractStart;
	}

	public void setAbstractStart(Date abstractStart) {
		this.abstractStart = abstractStart;
	}

	public Date getAbstractEnd() {
		return abstractEnd;
	}

	public void setAbstractEnd(Date abstractEnd) {
		this.abstractEnd = abstractEnd;
	}

	public HashMap<String, Double> getFees() {
		return Fees;
	}

	public void setFees(HashMap<String, Double> fees) {
		Fees = fees;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
