package org.registration.controller;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityNotFoundException;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.FeeEntity;
import org.registration.service.ConferenceManager;
import org.registration.service.FeeManager;
import org.registration.view.ConferenceInformation;
import org.registration.view.ConferenceNames;
import org.registration.view.Confirmation;
import org.registration.view.FeeType;
import org.registration.view.NewConference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		
		ConferenceEntity ce = conferenceManager.findByConferenceCode(conference_code);
		
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
	
	@GetMapping(value = "/allConferences")
	public List<ConferenceNames> getAllConferences() {
		
		List<ConferenceEntity> allConferences = conferenceManager.findAllConferences();
		
		List<ConferenceNames> allConfName = new ArrayList<ConferenceNames>();
		
		SimpleDateFormat f = new SimpleDateFormat("EEEEE, MMMMMM dd yyyy");
		
		for(ConferenceEntity ce : allConferences) {
			
			ConferenceNames conference1 = new ConferenceNames();
			conference1.setConferenceName(ce.getConferenceName());
			conference1.setId(ce.getConferenceId());
			conference1.setRegistrationStart(f.format(new Date(ce.getRegistrationStart().getTime())));
			conference1.setRegistrationEnd(f.format(new Date(ce.getRegistrationEnd().getTime())));
			allConfName.add(conference1);
		}
				
		return allConfName;
	}
	
	@GetMapping(value = "/details/{conferenceId}")
	public ConferenceEntity getDetailedConferenceInfo(@PathVariable Long conferenceId) {
		
		ConferenceEntity ce = conferenceManager.findByConferenceId(conferenceId);
		
		if(ce == null) {
			throw new EntityNotFoundException();
		}
		
		return ce;	
	}
	
	@PostMapping(value = "/addNew")
	public Confirmation addNewConference(@RequestBody(required=true) NewConference nc) {
		
		String newConferenceCode = this.getNewConferenceCode();
		ConferenceEntity ce = conferenceManager.findByConferenceCode(newConferenceCode);
		
		while (ce!=null) {
			newConferenceCode = this.getNewConferenceCode();
			ce = conferenceManager.findByConferenceCode(newConferenceCode);
		}
		
		ce = new ConferenceEntity();
		
		ce.setConferenceCode(newConferenceCode);		
		ce.setConferenceName(nc.getConferenceName());
		ce.setRegistrationStart(java.sql.Timestamp.valueOf(nc.getRegistrationStartDate()));
		ce.setRegistrationEnd(java.sql.Timestamp.valueOf(nc.getRegistrationEndDate()));
		ce.setAbstractStart(java.sql.Timestamp.valueOf(nc.getAbstractStartDate()));
		ce.setAbstractEnd(java.sql.Timestamp.valueOf(nc.getAbstractEndDate()));
		ce.setPostRegistrationCode(this.getNewConferenceCode());
		ce.setEmailList(nc.getEmailList());
		ce.setConfirmationEmail(nc.getConfirmationEmail());
		ce.setShortTalks(nc.isShortTalks());
		
		conferenceManager.createConference(ce);
		
		List<FeeType> fees = nc.getFeeList();
		
		for(FeeType f : fees) {
			FeeEntity fe = new FeeEntity(f.getName(),f.getAmount());
			fe.setConferenceEntity(ce);
			feeManager.createFee(fe);
		}
		
		return new Confirmation("New Conference Added", HttpStatus.CREATED.value());
	}
	
	public String getNewConferenceCode() {
		
		int leftLimit = 65; // letter 'A'
	    int rightLimit = 90; // letter 'Z'
	    int targetStringLength = 10;
		Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
	 	    
	    return generatedString;
	}	
	
	@PutMapping(value="/update/{conference_Id}")
	public Confirmation updateConferenceInfo(@RequestBody(required=true) NewConference nc, 
												@PathVariable Long conferenceId) {
		
		ConferenceEntity ce = conferenceManager.findByConferenceId(conferenceId);
		
		if(ce == null) {
			throw new EntityNotFoundException();
		}
		
		ce.setConferenceName(nc.getConferenceName());
		ce.setRegistrationStart(java.sql.Timestamp.valueOf(nc.getRegistrationStartDate()));
		ce.setRegistrationEnd(java.sql.Timestamp.valueOf(nc.getRegistrationEndDate()));
		ce.setAbstractStart(java.sql.Timestamp.valueOf(nc.getAbstractStartDate()));
		ce.setAbstractEnd(java.sql.Timestamp.valueOf(nc.getAbstractEndDate()));
		ce.setPostRegistrationCode(this.getNewConferenceCode());
		ce.setEmailList(nc.getEmailList());
		ce.setConfirmationEmail(nc.getConfirmationEmail());
		ce.setShortTalks(nc.isShortTalks());
		
		conferenceManager.createConference(ce);
		
		List<FeeType> fees = nc.getFeeList();
		
		for(FeeType f : fees) {
			
			FeeEntity fe = feeManager.findByNameAndConferenceEntity(f.getName(), ce);
			
			if(fe != null) {
				fe.setName(f.getName());
				fe.setAmount(f.getAmount());
				feeManager.createFee(fe);
			} else {
				fe = new FeeEntity(f.getName(),f.getAmount());
				fe.setConferenceEntity(ce);
				feeManager.createFee(fe);
			}			 
		}
		
		return new Confirmation("New Conference Added", HttpStatus.CREATED.value());	
	}
	
	
}