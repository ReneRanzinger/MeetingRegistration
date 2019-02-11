package org.registration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.CONFLICT)  // 409
public class ParticipantsExistsException extends RuntimeException {

	public ParticipantsExistsException() { super(); }
	public ParticipantsExistsException(String s) { super(s); }
	public ParticipantsExistsException(String s, Throwable throwable) { super(s, throwable); }
	public ParticipantsExistsException(Throwable throwable) { super(throwable); }
}
