package org.registration.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.ParticipantEntity;
import org.registration.service.ConferenceManager;
import org.registration.service.ParticipantManager;
import org.registration.view.Confirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participant")
public class ParticipantController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	ParticipantManager participantManager;
	
	@GetMapping(value="/allparticipants/{conferenceId}")
	public List<ParticipantEntity> getAllParticipants(@PathVariable Long conferenceId) {
		
		  ConferenceEntity ce = conferenceManager.findByConferenceId(conferenceId);
		  
		  if(ce == null) {
				throw new EntityNotFoundException();
			}
		  
		  List<ParticipantEntity> allParticipants = participantManager.findAllParticipantsByConference(ce);
		
		  return allParticipants;
	}
	
	@DeleteMapping(value="/delete/{participantId}")
	public Confirmation deleteParticipant(@PathVariable Long participantId) {
		
		ParticipantEntity pe = participantManager.findByParticipantId(participantId);
		
		 if(pe == null) {
				throw new EntityNotFoundException();
			}
		
		participantManager.deleteParticipantById(participantId);
		
		return new Confirmation("Participant Deleted Successfully", HttpStatus.OK.value());
	}
		
}
