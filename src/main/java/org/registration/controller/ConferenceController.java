package org.registration.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.registration.view.ConferenceInformation;
import org.registration.view.FeeTypes;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conference")
public class ConferenceController {

	@CrossOrigin
	@GetMapping("/info/{conference_code}")
	public ConferenceInformation getConferenceInfo(@PathVariable String conference_code) {
		
		ConferenceInformation ci = new ConferenceInformation(conference_code);
		Date registrationStart = new Date(), registrationEnd = new Date(), abstractEnd = new Date(), abstractStart = new Date();
		
		if (conference_code.equals(new String("A01"))) {
			registrationStart = new GregorianCalendar(2018, Calendar.JUNE, 1).getTime();
			 registrationEnd =new GregorianCalendar(2018, Calendar.JUNE, 30).getTime();
			 abstractStart = new GregorianCalendar(2018, Calendar.JUNE, 15).getTime();
			 abstractEnd = new GregorianCalendar(2018, Calendar.JUNE, 25).getTime();
		} else if (conference_code.equals(new String("A02"))) {		
		 registrationStart = new GregorianCalendar(2018, Calendar.JULY, 1).getTime();
		 registrationEnd =new GregorianCalendar(2018, Calendar.JULY, 31).getTime();
		 abstractStart = new GregorianCalendar(2018, Calendar.JULY, 15).getTime();
		 abstractEnd = new GregorianCalendar(2018, Calendar.JULY, 25).getTime();
		}else if (conference_code.equals(new String("A03"))) {
			registrationStart = new GregorianCalendar(2018, Calendar.AUGUST, 1).getTime();
			 registrationEnd =new GregorianCalendar(2018, Calendar.AUGUST, 31).getTime();
			 abstractStart = new GregorianCalendar(2018, Calendar.AUGUST, 15).getTime();
			 abstractEnd = new GregorianCalendar(2018, Calendar.AUGUST, 25).getTime();
		}
		
		ci.setRegistrationStart(registrationStart);
		ci.setRegistrationEnd(registrationEnd);
		ci.setAbstractStart(abstractStart);
		ci.setAbstractEnd(abstractEnd);
		
		List<FeeTypes> fees = new ArrayList<FeeTypes>();
		
		FeeTypes f1 = new FeeTypes("Standard Fee",1000.0);
		FeeTypes f2 = new FeeTypes("UGA Employee Fee",700.0);
		FeeTypes f3 = new FeeTypes("Federal Employee Fee",500.0);
			
		fees.add(f3);
		fees.add(f2);
		fees.add(f1);
		
		ci.setFees(fees);
		
		int statusCode=0;
		
		if(new Date().compareTo(registrationStart) < 0) {
			statusCode=1;
		} else if(new Date().compareTo(registrationEnd) > 0) {
			statusCode=-1;
		} else if (new Date().compareTo(registrationStart) >= 0 || new Date().compareTo(registrationEnd) <= 0) {
			statusCode=0;
		}
		
		ci.setStatusCode(statusCode);
		
		return ci;	
	}
	
	
}