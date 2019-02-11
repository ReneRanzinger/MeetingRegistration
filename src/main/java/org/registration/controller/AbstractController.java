package org.registration.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.ParticipantEntity;
import org.registration.service.ConferenceManager;
import org.registration.service.ParticipantManager;
import org.registration.view.ConferenceInformation;
import org.registration.view.Confirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Abstract controller. This class includes RESTFUL web services to handle submission
 * of abstract files for conferences by participants.
 * 
 * @author vinamra
 */
@RestController
@RequestMapping("/abstract")
public class AbstractController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	ParticipantManager participantManager;
	
	/**
	 * Web service to get information conference information with the status code,
	 * showing status of the abstract submission schedule. If no conference exists
	 * with given conference code, entity not found exception is thrown. 
	 * 
	 *  statusCode = -1 i.e. abstract submission for following conference is over.
	 *  statusCode = 0	i.e. abstract submission for following conference is online.
	 *  statusCode = 1	i.e. abstract submission for following conference is not started.
	 *  
	 *  Web service Endpoint: /abstract/info/{conference_code}
	 *  
	 *  Authentication : Not required
	 *  
	 * @param conference_code
	 * @return ConferenceInformation class object.
	 * @throws EntityNotFoundException
	 * 
	 */
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
		
		int statusCode = 0;
		
		if(new Timestamp(System.currentTimeMillis()).compareTo(ce.getAbstractStart()) < 0) {
			statusCode=1;
		} else if(new Timestamp(System.currentTimeMillis()).compareTo(ce.getAbstractEnd()) > 0) {
			statusCode=-1;
		} else if (new Timestamp(System.currentTimeMillis()).compareTo(ce.getAbstractStart()) >= 0 && new Date().compareTo(ce.getAbstractEnd()) <= 0) {
			statusCode=0;
		}
		
		if(post_reg_code.isPresent() && post_reg_code.get().equals(ce.getPostRegistrationCode())) {
			statusCode=0;
		}
		
		ci.setStatusCode(statusCode);
		
		return ci;
		
	}
	
	/**
	 * 
	 * Web service to accept the multipart form-data and perform the validation 
	 * for existing participant. If there exists a participant with given confirmation
	 * number and email address then save the abstract file submitted with the form
	 * data else throws an entity not found exception.
	 * 
	 * Web service Endpoint: /abstract/add
	 * 
	 * Authentication : Not required
	 * 
	 * @param confirmationNumber
	 * @param email
	 * @param abstractTitle
	 * @param file
	 * @param considerTalk
	 * @return Confirmation class object.
	 * @throws IOException
	 * @throws EntityNotFoundException
	 */
	@RequestMapping(value = "/add", method=RequestMethod.POST, consumes = {"multipart/form-data"})
	public Confirmation addParticipantAbstract(@RequestPart("confirmationNumber") String confirmationNumber,
			@RequestPart("email") String email,
			@RequestPart("abstractTitle") String abstractTitle,
			@RequestPart("file") MultipartFile file,
			@RequestPart("considerTalk") String considerTalk) throws IOException {
		
		ParticipantEntity pe = participantManager.findByParticipantIdAndEmail(Long.parseLong(confirmationNumber.trim()), email.trim());
		
		if(pe == null) {
			throw new EntityNotFoundException();
		}
		
		pe.setAbstractTitle(abstractTitle);
		pe.setAbstractFileName(file.getContentType()); 
		pe.setAbstrct(file.getBytes());
		pe.setConsiderTalk(Boolean.parseBoolean(considerTalk));
		participantManager.createParticipant(pe);
		
		return new Confirmation("Abstract successfully added", HttpStatus.ACCEPTED.value());
	}
	
}
