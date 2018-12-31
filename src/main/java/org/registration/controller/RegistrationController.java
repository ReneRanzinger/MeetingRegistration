package org.registration.controller;

import java.sql.Timestamp;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.registration.exception.EmailExistsException;
import org.registration.exception.PromoCodeNotFoundException;
import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.FeeEntity;
import org.registration.persistence.ParticipantEntity;
import org.registration.persistence.PromotionCodeEntity;
import org.registration.persistence.dao.ParticipantRepository;
import org.registration.persistence.dao.PromoCodeRepository;
import org.registration.service.ConferenceManager;
import org.registration.service.EmailManager;
import org.registration.service.FeeManager;
import org.registration.service.ParticipantManager;
import org.registration.view.Confirmation;
import org.registration.view.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
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
	
	@Autowired
	ParticipantRepository participantRepository;
	
	@Autowired
	EmailManager emailManager;
	
	@Autowired
	PromoCodeRepository promoCodeRepository;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, 
    		consumes={"application/xml", "application/json"})
	public Confirmation register(@RequestBody(required=true) Participant p) {
		
			System.out.println(p);
		
			ParticipantEntity newParticipant = new ParticipantEntity(); 
			ConferenceEntity ce = conferenceManager.findByConferenceCode(p.getConferenceCode());
			
			if(ce == null) {
				throw new EntityNotFoundException();
			}
			
			newParticipant.setConference(ce);
			newParticipant.setFirstName(p.getFirstName().trim());
			newParticipant.setMiddleName(p.getMiddleName().trim());
			newParticipant.setLastName(p.getLastName().trim());
			newParticipant.setAddress(p.getAddress().trim());
			newParticipant.setDepartment(p.getDepartment().trim());
			newParticipant.setInstitution(p.getInstitution().trim());
			newParticipant.setEmail(p.getEmail().toLowerCase());
			newParticipant.setPhone(p.getPhone().trim());
			newParticipant.setTitle(p.getTitle().trim());
			newParticipant.setProfession(p.getProfession().trim());
			newParticipant.setPromotionCode(p.getPromotionCode().trim());
			
			String feeName = p.getFee().getName().trim();
			FeeEntity fe = feeManager.findByNameAndConferenceEntity(feeName, ce);
						
			newParticipant.setFee(fe);
			newParticipant.setComment(p.getComment().trim());
			newParticipant.setRegistrationTime(new Timestamp(System.currentTimeMillis()));
			newParticipant.setPayed(false);
			newParticipant.setDiet(p.getDiet().trim());
			
			ParticipantEntity existing = participantRepository.findByFirstNameAndMiddleNameAndLastNameAndConference(p.getFirstName(), p.getMiddleName(), p.getLastName(), ce);
			if (existing != null) {
				throw new EntityExistsException (p.getFirstName() +" "+ p.getMiddleName() +" "+ p.getLastName() + " is already a registered Participant for this meeting!");
			}
			
			existing = participantRepository.findByEmailAndConference(p.getEmail().toLowerCase(), ce);
			
			if (existing != null) {
				throw new EmailExistsException ("There is already a registered participant with this email: " + p.getEmail());
			}
			
			if(p.getPromotionCode().trim().length() != 0) {
				PromotionCodeEntity code = promoCodeRepository.findByCodeAndConference(p.getPromotionCode().trim(), ce);
				if(code == null) {
					throw new PromoCodeNotFoundException(p.getPromotionCode().trim()+" is not valid promo code for this meeting");
				}
			}
			participantManager.createParticipant(newParticipant);
        	String confirmationText;
			try {
	        	confirmationText = emailManager.sendConfirmationEmail(newParticipant);
	        } catch (MailSendException e) {
	        	throw e;
	        }
			
			return new Confirmation(confirmationText, HttpStatus.CREATED.value());			
	}
		
}
