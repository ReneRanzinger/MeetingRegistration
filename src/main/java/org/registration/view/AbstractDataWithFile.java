package org.registration.view;

import org.springframework.web.multipart.MultipartFile;

public class AbstractDataWithFile {

	private long confirmationNumber;
	private String email;
	private String abstractTitle;
	private MultipartFile abstractFile;
	boolean considerTalk;
	
	public long getConfirmationNumber() {
		return confirmationNumber;
	}
	public void setConfirmationNumber(long confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAbstractTitle() {
		return abstractTitle;
	}
	public void setAbstractTitle(String abstractTitle) {
		this.abstractTitle = abstractTitle;
	}
	public MultipartFile getAbstractFile() {
		return abstractFile;
	}
	public void setAbstractFile(MultipartFile abstractFile) {
		this.abstractFile = abstractFile;
	}
	public boolean isConsiderTalk() {
		return considerTalk;
	}
	public void setConsiderTalk(boolean considerTalk) {
		this.considerTalk = considerTalk;
	}
	
}
