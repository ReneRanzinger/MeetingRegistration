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

/**
 * Fee controller. REST Controller to regulate adding, updating and deleting
 * fees for conferences in database. Adding, updating and deleting a fee entity
 * requires a valid conference to be added and updated separately. Fee can only be added
 * once conference is saved with unique code and unique Id. Also, a fee entity can
 * only be deleted if no participants have registered with this fee yet.
 * 
 * @author vinamra
 *
 */
@RestController
@RequestMapping("/fee")
public class FeeController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	FeeManager feeManager;
	
	@Autowired
	ParticipantManager participantManager;
	
	/**
	 * Web service to add a new fee entity for an existing conference. Fee entities
	 * can only be added once the conference is successfully added and assigned an unique
	 * conference Id.
	 * 
	 * Web service end point: /fee/addNew/{conferenceId}
	 * 
	 * Authorization: required
	 * 
	 * @param conferenceId
	 * @param NewFee object. 
	 * @return Confirmation object.
	 * @throws EntityNotFoundException
	 */
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
	
	/**
	 * Web service to update a fee entity. Fee entities are update separately than 
	 * updating a conference. 
	 * 
	 * Web service end point: /fee/update/{feeId}
	 * 
	 * Authorization: required
	 * 
	 * @param feeId
	 * @param NewFee object
	 * @return Confirmation object
	 * @throws EntityNotFoundException
	 */
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
	
	/**
	 * Web service to delete a fee entity. A fee entity can only be deleted if no 
	 * participants have already registered using this fee entity.
	 * 
	 * Web service end point: /fee/delete/{feeId}
	 * 
	 * Authorization: required
	 * @param feeId
	 * @return Confirmation object.
	 * @throws EntityNotFoundException
	 * @throws ParticipantsExistsException
	 */
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
