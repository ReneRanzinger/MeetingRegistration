package org.registration.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.registration.view.ConferenceInformation;
import org.registration.view.Confirmation;
import org.registration.view.NewFee;
import org.registration.view.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Registration Controller. REST controller to enable registration of participants
 * for specific conferences. This controller is for client side applications, hence,
 * Authorization is not required
 * 
 * @author vinamra
 *
 */
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
	
	/**
	 * 
	 * Web service to get conference information with the status code,
	 * showing status of the conference registration schedule. If no conference exists
	 * with given conference code, entity not found exception is thrown. 
	 * 
	 * statusCode = -1 i.e. registration for following conference is over.
	 *  statusCode = 0	i.e. registration for following conference is online.
	 *  statusCode = 1	i.e. registration for following conference is not started.
	 *  
	 *  Web service Endpoint: /registration/info/{conference_code}
	 *  Web service Endpoint: /registration/info/{conference_code}/{post_reg_code}
	 * 
	 *  Authentication : Not required
	 * 
	 * @param conference_code
	 * @param post_reg_code (optional)
	 * @return ConferenceInformation class object.
	 * @throws EntityNotFoundException
	 */
	@GetMapping(value = {"/info/{conference_code}","/info/{conference_code}/{post_reg_code}"})
	public ConferenceInformation getConferenceInfo(@PathVariable String conference_code, @PathVariable Optional<String> post_reg_code) {
		
		ConferenceEntity ce = conferenceManager.findByConferenceCode(conference_code);
		
		if(ce == null) {
			throw new EntityNotFoundException("Conference Not Found. Please use the correct conference code.");
		}
		
		ConferenceInformation ci = new ConferenceInformation(conference_code);
		SimpleDateFormat f = new SimpleDateFormat("EEEEE, MMMMMM dd yyyy");
		
		ci.setRegistrationStart(f.format(new Date(ce.getRegistrationStart().getTime())));
		ci.setRegistrationEnd(f.format(new Date(ce.getRegistrationEnd().getTime())));
		ci.setAbstractEnd(f.format(new Date(ce.getAbstractEnd().getTime())));
		ci.setAbstractStart(f.format(new Date(ce.getAbstractStart().getTime())));
		
		List<NewFee> fees = new ArrayList<NewFee>();
		
		List<FeeEntity> feeEntities = feeManager.findByConferenceCode(conference_code);
		
		for (FeeEntity fe : feeEntities) {
			NewFee temp = new NewFee(fe.getName(), fe.getAmount());
			fees.add(temp);
		}
		
		ci.setFees(fees);
		
		int statusCode = 0;
		
		if(new Timestamp(System.currentTimeMillis()).compareTo(ce.getRegistrationStart()) < 0) {
			statusCode=1;
		} else if(new Timestamp(System.currentTimeMillis()).compareTo(ce.getRegistrationEnd()) > 0) {
			statusCode=-1;
		} else if (new Timestamp(System.currentTimeMillis()).compareTo(ce.getRegistrationStart()) >= 0 && new Date().compareTo(ce.getRegistrationEnd()) <= 0) {
			statusCode=0;
		}
		
		if(post_reg_code.isPresent() && post_reg_code.get().equals(ce.getPostRegistrationCode())) {
			statusCode=0;
		}
		
		ci.setStatusCode(statusCode);
		
		return ci;
		
	}
	
	/**
	 * Web service to enable client/participant registrations. It receives
	 * a new participant object, perform checks for validity of conference,
	 * unique registrations and valid promo codes. If every thing is valid
	 * it register the new participant into database and send the participant a
	 * confirmation email.
	 * 
	 * Web service end point: /registration/register
	 * 
	 * Authorization: not required
	 * @param Participant object
	 * @return Confirmation Object
	 * @throws EntityNotFoundException
	 * @throws EntityExistsException
	 * @throws MailSendException
	 * @throws PromoCodeNotFoundException
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, 
    		consumes={"application/xml", "application/json"})
	public Confirmation register(@RequestBody(required=true) Participant p) {
		
			ParticipantEntity newParticipant = new ParticipantEntity(); 
			ConferenceEntity ce = conferenceManager.findByConferenceCode(p.getConferenceCode());
			
			if(ce == null) {
				throw new EntityNotFoundException("Conference Not Found. Please use the correct conference code.");
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
	        	return new Confirmation("Thank you for registering. Could not send"
	        			+ "a confirmation email at the moment."
	        			+ "Please contact the organizers if you have any further questions.", HttpStatus.CREATED.value());
	        }
			
			return new Confirmation(confirmationText, HttpStatus.CREATED.value());			
	}
		
}
