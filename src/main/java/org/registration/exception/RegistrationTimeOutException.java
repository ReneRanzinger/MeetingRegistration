package org.registration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.CONFLICT)  // 409
public class RegistrationTimeOutException extends RuntimeException {
	
	public RegistrationTimeOutException() { super(); }
	public RegistrationTimeOutException(String s) { super(s); }
	public RegistrationTimeOutException(String s, Throwable throwable) { super(s, throwable); }
	public RegistrationTimeOutException(Throwable throwable) { super(throwable); }

}
