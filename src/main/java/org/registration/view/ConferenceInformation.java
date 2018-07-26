package org.registration.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.registration.persistence.FeeEntity;

public class ConferenceInformation {

	private String conferenceCode;
	private Date registrationStart;
	private Date registrationEnd;
	private Date abstractStart;
	private Date abstractEnd;
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
