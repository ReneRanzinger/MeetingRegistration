package org.registration.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityNotFoundException;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.ParticipantEntity;
import org.registration.service.ConferenceManager;
import org.registration.service.ParticipantManager;
import org.registration.view.AbstractDataWithFile;
import org.registration.view.ConferenceInformation;
import org.registration.view.Confirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * 
 * 
 * @author vinamra
 *
 */
@RestController
@RequestMapping("/abstract")
public class AbstractController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	ParticipantManager participantManager;
	
	@GetMapping(value = "/info/{conference_code}")
	public ConferenceInformation getConferenceInfo(@PathVariable String conference_code) {
		
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
//		
//		if(new Timestamp(System.currentTimeMillis()).compareTo(ce.getAbstractStart()) < 0) {
//			statusCode=1;
//		} else if(new Timestamp(System.currentTimeMillis()).compareTo(ce.getAbstractEnd()) > 0) {
//			statusCode=-1;
//		} else if (new Timestamp(System.currentTimeMillis()).compareTo(ce.getAbstractStart()) >= 0 && new Date().compareTo(ce.getAbstractEnd()) <= 0) {
//			statusCode=0;
//		}
//		
		ci.setStatusCode(statusCode);
		
		return ci;
		
	}
	
	@PostMapping(value = "/add")
	public Confirmation addParticipantAbstract(@RequestParam MultipartFile abstractFile) {
		
//		ParticipantEntity pe = participantManager.findByParticipantIdAndEmail(data.getConfirmationNumber(), data.getEmail());
//		
//		if(pe == null) {
//			throw new EntityNotFoundException();
//		}
		
		String result = null;
        try {
            result = this.saveUploadedFiles(abstractFile);

           // result = this.saveUploadedFiles(data.getAbstractFile());
        	System.out.println("File reached here" + result);
 
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Confirmation("Abstract not added", HttpStatus.BAD_REQUEST.value());
        }
		
		
		return new Confirmation("Abstract successfully added", HttpStatus.ACCEPTED.value());
	}
	
	
	private String saveUploadedFiles(MultipartFile file) throws IOException {
		 String UPLOAD_DIR = "/Users/vinamra/Desktop/test";
        // Make sure directory exists!
        File uploadDir = new File(UPLOAD_DIR);
        uploadDir.mkdirs();
 
        StringBuilder sb = new StringBuilder();
            
            String uploadFilePath = UPLOAD_DIR + "/" + file.getOriginalFilename();
 
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFilePath);
            Files.write(path, bytes);
 
            sb.append(uploadFilePath).append("<br/>");
       
        return sb.toString();
    }
}
