package org.registration.controller;

import java.util.List;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.ParticipantEntity;
import org.registration.persistence.dao.ParticipantRepository;
import org.registration.service.ConferenceManager;
import org.registration.view.ParticipantsExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	ParticipantRepository participantRepository;	
	
	@GetMapping(value="/participantExcel/{conference_code}")
	public ModelAndView getParticipantExcelList(@PathVariable String conference_code) {
		
		ConferenceEntity ce = conferenceManager.findByConferenceCode(conference_code);
		
		List<ParticipantEntity> participants = participantRepository.findByConference(ce);
		
		return new ModelAndView(new ParticipantsExcelView(), "participants", participants);
	}
	
}
