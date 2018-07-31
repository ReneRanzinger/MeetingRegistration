package org.registration.controller;

import java.sql.Timestamp;

import org.registration.persistence.FeeEntity;
import org.registration.persistence.ParticipantEntity;
import org.registration.persistence.dao.ConferenceRepository;
import org.registration.persistence.dao.FeeRepository;
import org.registration.service.ConferenceManager;
import org.registration.service.FeeManager;
import org.registration.service.ParticipantManager;
import org.registration.view.Confirmation;
import org.registration.view.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	ParticipantManager participantManager;
	
	@Autowired
	FeeManager feeManager;
	
	@CrossOrigin
	@RequestMapping(value = "/register", method = RequestMethod.POST, 
    		consumes={"application/xml", "application/json"})
	public Confirmation register(@RequestBody(required=true) Participant p) {
		
			System.out.println(p);
		
		try {
			ParticipantEntity newParticipant = new ParticipantEntity(); 
			newParticipant.setConference(conferenceManager.findByConferenceCode(p.getConferenceCode()));
			newParticipant.setFirstName(p.getFirstName());
			newParticipant.setMiddleName(p.getMiddleName());
			newParticipant.setLastName(p.getLastName());
			newParticipant.setAddress(p.getAddress());
			newParticipant.setDepartment(p.getDepartment());
			newParticipant.setInstitution(p.getInstitution());
			newParticipant.setEmail(p.getEmail());
			newParticipant.setPhone(p.getPhone());
			newParticipant.setTitle(p.getTitle());
			newParticipant.setProfession(p.getProfession());
			newParticipant.setPromotionCode(p.getPromotionCode());
			
			FeeEntity fe = feeManager.findByNameAndConferenceEntity(p.getFee().getName(), conferenceManager.findByConferenceCode(p.getConferenceCode()));
						
			newParticipant.setFee(fe);
			newParticipant.setComment(p.getComment());
			newParticipant.setRegistrationTime(new Timestamp(System.currentTimeMillis()));
			newParticipant.setPayed(false);
			newParticipant.setDiet(p.getDiet());
			
			participantManager.addParticipant(newParticipant);
			
			
			return new Confirmation("User added successfully", HttpStatus.CREATED.value());
		} catch (Exception e) {
			return new Confirmation("User added successfully", HttpStatus.CREATED.value());
		}
		
		
		
		
	}
	
	
	
}
