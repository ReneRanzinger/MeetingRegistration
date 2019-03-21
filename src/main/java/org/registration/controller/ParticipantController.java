package org.registration.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.ParticipantEntity;
import org.registration.service.ConferenceManager;
import org.registration.service.ParticipantManager;
import org.registration.view.Confirmation;
import org.registration.view.ParticipantsExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Participant controller. REST Controller to view all the registered partcipants 
 * for any conference and delete a participant if required.
 * 
 * @author vinamra
 *
 */
@RestController
@RequestMapping("/participant")
public class ParticipantController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	ParticipantManager participantManager;
	
	/**
	 * Web service to view all the registered participants for a conference.
	 * 
	 * Web service end point: /participant/allParticipants/{conferenceId}
	 * 
	 * Authorization: required
	 * @param conferenceId
	 * @return List of Participant entities as JSON object.
	 * @throws EntityNotFoundException
	 */
	@GetMapping(value="/allParticipants/{conferenceId}")
	public List<ParticipantEntity> getAllParticipants(@PathVariable Long conferenceId) {
		
		  ConferenceEntity ce = conferenceManager.findByConferenceId(conferenceId);
		  
		  if(ce == null) {
				throw new EntityNotFoundException("Conference Not Found. Please use the correct conference Id.");
			}
		  
		  List<ParticipantEntity> allParticipants = participantManager.findAllParticipantsByConference(ce);
		
		  return allParticipants;
	}
	
	/**
	 * Web service to update fees payed or not by a a participant with unique 
	 * participant Id.
	 * 
	 * Web service end point: /participant/update/{participantId}
	 * 
	 * Authorization: required
	 * 
	 * @param participantId
	 * @return Confirmation object
	 * @throws EntityNotFoundException
	 */
	@PutMapping(value="/update/{participantId}")
	public Confirmation updateParticipant(@PathVariable Long participantId, 
			@RequestBody(required=true) String status ) {
		
		ParticipantEntity pe = participantManager.findByParticipantId(participantId);
		
		 if(pe == null) {
				throw new EntityNotFoundException("Participant Not Found. Please use the correct participant Id.");
			}
		
		pe.setPayed(Boolean.parseBoolean(status.toLowerCase().trim()));
		
		participantManager.createParticipant(pe);
		
		return new Confirmation("Participant Updated Successfully", HttpStatus.OK.value());
	}
	
	/**
	 * Web service to delete a participant with unique participant Id.
	 * 
	 * Web service end point: /participant/delete/{participantId}
	 * 
	 * Authorization: required
	 * 
	 * @param participantId
	 * @return Confirmation object
	 * @throws EntityNotFoundException
	 */
	@DeleteMapping(value="/delete/{participantId}")
	public Confirmation deleteParticipant(@PathVariable Long participantId) {
		
		ParticipantEntity pe = participantManager.findByParticipantId(participantId);
		
		 if(pe == null) {
				throw new EntityNotFoundException("Participant Not Found. Please use the correct participant Id.");
			}
		
		participantManager.deleteParticipantById(participantId);
		
		return new Confirmation("Participant Deleted Successfully", HttpStatus.OK.value());
	}
	
	/**
	 * 
	 * Webservice to download all the participants for a conference into microsoft excel
	 * file and save it to local mac or pc.
	 * 
	 * Web service Endpoint: /participant/download/{conference_code}
	 * 
	 * Authentication : Required
	 * 
	 * @param conference_code
	 * @return ModelAndView object.
	 */
	@GetMapping(value="/download/{conferenceId}")
	public ModelAndView getParticipantExcelList(@PathVariable Long conferenceId) {
		
		ConferenceEntity ce = conferenceManager.findByConferenceId(conferenceId);
		
		 if(ce == null) {
				throw new EntityNotFoundException("Conference Not Found. Please use the correct conference Id.");
		}
		 
		List<ParticipantEntity> participants = participantManager.findAllParticipantsByConference(ce);
		
		return new ModelAndView(new ParticipantsExcelView(), "participants", participants);
	}
		
	/**
	 * Web service to download the abstract for a participants.
	 * 
	 * Endpoint: /participant/downloadAbstract/{participantId}
	 * 
	 * Authorization: required
	 * 
	 * @param participantId
	 * @return ResponseEntity with status ok and attachment of the Abstract file.
	 * @throws EntityNotFoundException
	 */
	@GetMapping(value="/downloadAbstract/{participantId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long participantId) {
		
		ParticipantEntity pe = participantManager.findByParticipantId(participantId);
		
		if(pe == null) {
			throw new EntityNotFoundException("Participant Not Found. Please use the correct participant Id.");
		}
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(pe.getAbstractFileType()))
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"abstract_"+pe.getFirstName()+pe.getLastName()+"("+pe.getParticipantId()+")\"")
						.body(new ByteArrayResource(pe.getAbstrct()));
	}
	
	/**
	 * Web service to download all participant's abstracts as a zip file for a given 
	 * conference.
	 * 
	 * Endpoint: /participant/downloadAllAbstracts/{conferenceId}
	 * 
	 * Authorization: required
	 * 
	 * @param conferenceId
	 * @return ResponseEntity with status ok and attachment of a zip file.
	 * @throws IOException 
	 * @throws EntityNotFoundException
	 */
	@GetMapping(value="/downloadAllAbstracts/{conferenceId}", produces="application/zip")
	public ResponseEntity<Resource> downloadAllAbstracts(@PathVariable Long conferenceId) throws IOException{

		ConferenceEntity ce = conferenceManager.findByConferenceId(conferenceId);
		 
		if(ce == null) {
			throw new EntityNotFoundException("Conference Not Found. Please use the correct conference Id.");
		}
	  
	  List<ParticipantEntity> allParticipants = participantManager.findAllParticipantsByConference(ce);
	  	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(baos);
		for(ParticipantEntity pe : allParticipants) {
			if(pe.getAbstrct()!=null) {
				ZipEntry entry = new ZipEntry("abstract_("+pe.getParticipantId()+")"+pe.getAbstractFileName());
			    entry.setSize(pe.getAbstrct().length);
			    zos.putNextEntry(entry);
//			    ByteArrayInputStream bios = new ByteArrayInputStream(pe.getAbstrct());
//			   
//			    IOUtils.copy(bios, zos);
			    zos.write(pe.getAbstrct());
			    zos.closeEntry();
			}
		}
		zos.flush();
		zos.close();
	
	return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType("application/zip"))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test\"")
			.body(new ByteArrayResource(baos.toByteArray()));
	}
}
