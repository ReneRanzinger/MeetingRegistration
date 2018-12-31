package org.registration.view;

public class ConferenceNames {

	private Long id;
	private String conferenceName;
	private String registrationStart;
	private String registrationEnd;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getConferenceName() {
		return conferenceName;
	}
	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
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
}
