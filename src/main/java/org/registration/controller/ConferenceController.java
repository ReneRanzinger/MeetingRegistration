package org.registration.controller;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.FeeEntity;
import org.registration.service.ConferenceManager;
import org.registration.service.FeeManager;
import org.registration.view.ConferenceInformation;
import org.registration.view.FeeType;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@GetMapping(value = {"/info/{conference_code}","/info/{conference_code}/{post_reg_code}"})
	public ConferenceInformation getConferenceInfo(@PathVariable String conference_code, @PathVariable Optional<String> post_reg_code) {
		
		ConferenceEntity ce;
						
		ce = conferenceManager.findByConferenceCode(conference_code);
		
		if(ce == null) {
			throw new EntityNotFoundException();
		}
		
		ConferenceInformation ci = new ConferenceInformation(conference_code);
		SimpleDateFormat f = new SimpleDateFormat("EEEEE, MMMMMM dd yyyy");
		
		ci.setRegistrationStart(f.format(new Date(ce.getRegistrationStart().getTime())));
		ci.setRegistrationEnd(f.format(new Date(ce.getRegistrationEnd().getTime())));
		ci.setAbstractEnd(f.format(new Date(ce.getAbstractEnd().getTime())));
		ci.setAbstractStart(f.format(new Date(ce.getAbstractStart().getTime())));
		
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
		
		if(post_reg_code.isPresent() && post_reg_code.get().equals(ce.getPostRegistrationCode())) {
			statusCode=0;
		}
		
		ci.setStatusCode(statusCode);
		
		return ci;
		
	}

	
}