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

/**
 * Admin Controller. This class is a temporary controller for admin to download
 * all the participants for a conference into excel format.
 * 
 * @author vinamra
 *
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	ParticipantRepository participantRepository;	
	
	/**
	 * 
	 * Webservice t download all the participants for a conference into microsoft excel
	 * file and save it to local mac or pc.
	 * 
	 * Web service Endpoint: /admin/participantExcel/{conference_code}
	 * 
	 * Authentication : Required
	 * 
	 * @param conference_code
	 * @return ModelAndView object.
	 */
	@GetMapping(value="/participantExcel/{conference_code}")
	public ModelAndView getParticipantExcelList(@PathVariable String conference_code) {
		
		ConferenceEntity ce = conferenceManager.findByConferenceCode(conference_code);
		
		List<ParticipantEntity> participants = participantRepository.findByConference(ce);
		
		return new ModelAndView(new ParticipantsExcelView(), "participants", participants);
	}
	
}
