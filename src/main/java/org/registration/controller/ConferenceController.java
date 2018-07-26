package org.registration.controller;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.FeeEntity;
import org.registration.service.ConferenceManager;
import org.registration.service.FeeManager;
import org.registration.view.ConferenceInformation;
import org.registration.view.FeeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conference")
public class ConferenceController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	FeeManager feeManager;
	
	@CrossOrigin
	@GetMapping("/info/{conference_code}")
	public ConferenceInformation getConferenceInfo(@PathVariable String conference_code) {
		
		ConferenceEntity ce;
		try {
				
			ce = conferenceManager.findByConferenceCode(conference_code);
			
		}catch(Exception e) {
			
			return null;
		}
		
		ConferenceInformation ci = new ConferenceInformation(conference_code);
		
		ci.setRegistrationStart(ce.getRegistrationStart());
		ci.setRegistrationEnd(ce.getRegistrationEnd());
		ci.setAbstractEnd(ce.getAbstractEnd());
		ci.setAbstractStart(ce.getAbstractStart());
		
		List<FeeType> fees = new ArrayList<FeeType>();
		
		List<FeeEntity> feeEntities = feeManager.findByConferenceCode(conference_code);
		
		for (FeeEntity fe : feeEntities) {
			FeeType temp = new FeeType(fe.getName(), fe.getAmount());
			fees.add(temp);
		}
		
		ci.setFees(fees);
		
		int statusCode = 0;
		
		if(new Timestamp(System.currentTimeMillis()).compareTo(ce.getRegistrationStart()) < 0) {
			statusCode=1;
		} else if(new Timestamp(System.currentTimeMillis()).compareTo(ce.getRegistrationEnd()) > 0) {
			statusCode=-1;
		} else if (new Timestamp(System.currentTimeMillis()).compareTo(ce.getRegistrationStart()) >= 0 && new Date().compareTo(ce.getRegistrationEnd()) <= 0) {
			statusCode=0;
		}
		
		ci.setStatusCode(statusCode);
		
		return ci;
		
	}

	
}