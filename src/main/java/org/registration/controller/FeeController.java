package org.registration.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.registration.exception.ParticipantsExistsException;
import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.FeeEntity;
import org.registration.persistence.ParticipantEntity;
import org.registration.service.ConferenceManager;
import org.registration.service.FeeManager;
import org.registration.service.ParticipantManager;
import org.registration.view.Confirmation;
import org.registration.view.NewFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fee")
public class FeeController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	FeeManager feeManager;
	
	@Autowired
	ParticipantManager participantManager;
	
	@PostMapping(value="/addNew/{conferenceId}")
	public Confirmation addNewFee(@PathVariable Long conferenceId, @RequestBody(required=true) NewFee f) {
		
		ConferenceEntity ce = conferenceManager.findByConferenceId(conferenceId);
		
		if(ce == null) {
			throw new EntityNotFoundException();
		}
		
		FeeEntity fe = new FeeEntity(f.getName(),f.getAmount());
		fe.setConferenceEntity(ce);
				
		feeManager.createFee(fe);
		
		return new Confirmation("New Fee Added", HttpStatus.CREATED.value());
	}
	
	@PutMapping(value="/update/{feeId}")
	public Confirmation updateFee(@PathVariable Long feeId, @RequestBody(required=true) NewFee f) {
		
		FeeEntity fe = feeManager.findByFeeId(feeId);
		
		if(fe == null) {
			throw new EntityNotFoundException();
		}
		
		fe.setAmount(f.getAmount());
		fe.setName(f.getName());
		feeManager.createFee(fe);
		
		return new Confirmation("Fee Updated", HttpStatus.CREATED.value());
	}
	
	@DeleteMapping(value="/delete/{feeId}")
	public Confirmation deletefee(@PathVariable Long feeId) {
		
		FeeEntity fe = feeManager.findByFeeId(feeId);
		
		if(fe == null) {
			throw new EntityNotFoundException();
		}
		
		List<ParticipantEntity> allParticipants = participantManager.findAllParticipantsByFee(fe);
		
		if(allParticipants != null) {
			throw new ParticipantsExistsException("Cannot delte this fee. Atleast one participant already registered with this fee");
		}
		
		feeManager.deleteById(fe.getFeeId());
		
		return new Confirmation("Fee Deleted Successfully", HttpStatus.OK.value());
	}
}
