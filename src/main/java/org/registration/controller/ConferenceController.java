package org.registration.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.registration.view.ConferenceInformation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conference")
public class ConferenceController {

	
	@GetMapping("/info/{conference_code}")
	public ConferenceInformation getConferenceInfo(@PathVariable String conference_code) {
		
		ConferenceInformation ci = new ConferenceInformation(conference_code);
		Date registrationStart = new Date(), registrationEnd = new Date(), abstractEnd = new Date(), abstractStart = new Date();
		
		if (conference_code.equals(new String("A01"))) {		
		 registrationStart = new GregorianCalendar(2018, Calendar.JULY, 1).getTime();
		 registrationEnd =new GregorianCalendar(2018, Calendar.JULY, 31).getTime();
		 abstractStart = new GregorianCalendar(2018, Calendar.JULY, 15).getTime();
		 abstractEnd = new GregorianCalendar(2018, Calendar.JULY, 25).getTime();
		}else if (conference_code.equals(new String("A02"))) {
			registrationStart = new GregorianCalendar(2018, Calendar.AUGUST, 1).getTime();
			 registrationEnd =new GregorianCalendar(2018, Calendar.AUGUST, 31).getTime();
			 abstractStart = new GregorianCalendar(2018, Calendar.AUGUST, 15).getTime();
			 abstractEnd = new GregorianCalendar(2018, Calendar.AUGUST, 25).getTime();
		}else if (conference_code.equals(new String("A03"))) {
			registrationStart = new GregorianCalendar(2018, Calendar.SEPTEMBER, 1).getTime();
			 registrationEnd =new GregorianCalendar(2018, Calendar.SEPTEMBER, 30).getTime();
			 abstractStart = new GregorianCalendar(2018, Calendar.SEPTEMBER, 15).getTime();
			 abstractEnd = new GregorianCalendar(2018, Calendar.SEPTEMBER, 25).getTime();
		} 
		
		ci.setRegistrationStart(registrationStart);
		ci.setRegistrationEnd(registrationEnd);
		ci.setAbstractStart(abstractStart);
		ci.setAbstractEnd(abstractEnd);
		
		HashMap<String,Double> fees = new HashMap<String,Double>();
		
		fees.put("Standard Fee", 1000.00);
		fees.put("UGA Employee Fee", 700.00);
		fees.put("Federal Employee Fee", 500.00);
		
		ci.setFees(fees);
		
		int statusCode=0;
		
		if(new Date().compareTo(registrationStart) < 0) {
			statusCode=-1;
		} else if(new Date().compareTo(registrationEnd) > 0) {
			statusCode=1;
		} else if (new Date().compareTo(registrationStart) >= 0 || new Date().compareTo(registrationEnd) <= 0) {
			statusCode=0;
		}
		
		ci.setStatusCode(statusCode);
		
		return ci;	
	}
	
	@GetMapping("/hello")
	public String getHelloWorld() {
		return "Hello World";
	}
}