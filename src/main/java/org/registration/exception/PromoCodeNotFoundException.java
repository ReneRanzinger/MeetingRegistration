package org.registration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Entered promocode is not valid for this meeting")  // 404
public class PromoCodeNotFoundException extends RuntimeException {
	
	public PromoCodeNotFoundException() { super(); }
	public PromoCodeNotFoundException(String s) { super(s); }
	public PromoCodeNotFoundException(String s, Throwable throwable) { super(s, throwable); }
	public PromoCodeNotFoundException(Throwable throwable) { super(throwable); }
}
