package org.registration.controller;

import javax.persistence.EntityNotFoundException;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.PromotionCodeEntity;
import org.registration.service.ConferenceManager;
import org.registration.service.ParticipantManager;
import org.registration.service.PromoManager;
import org.registration.view.Confirmation;
import org.registration.view.NewPromoCode;
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
 * Promo code controller. REST Controller to regulate adding, updating and deleting
 * promo codes for existing conferences. Adding, updating and deleting a promo code
 * requires a valid conference to be added and updated separately. A promo code can 
 * only be added once conference is saved with unique Id.
 * 
 * @author vinamra
 *
 */
@RestController
@RequestMapping("/promo")
public class PromoController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	PromoManager promoManager;
	
	@Autowired
	ParticipantManager participantManager;

	/**
	 * Web service to add a new promo code entity for a conference. Prior to adding a
	 * new promo code the conference must be successfully added and assigned a unique
	 * conference Id.
	 * 
	 * Web service end point: /promo/addNew/{conferenceId}
	 * 
	 * Authorization: required
	 * 
	 * @param conferenceId
	 * @param NewPromoCode object
	 * @return Confirmation object
	 * @throws EntityNotFoundException
	 */
	@PostMapping(value="/addNew/{conferenceId}")
	public Confirmation addNewFee(@PathVariable Long conferenceId, @RequestBody(required=true) NewPromoCode newPromoCode) {
		
		ConferenceEntity ce = conferenceManager.findByConferenceId(conferenceId);
		
		if(ce == null) {
			throw new EntityNotFoundException();
		}
		
		PromotionCodeEntity pce = new PromotionCodeEntity(newPromoCode.getCode(), newPromoCode.getDescription());
		pce.setConference(ce);
		promoManager.createPromoCodeEntity(pce);
		
		return new Confirmation("New promo code added", HttpStatus.CREATED.value());
	}
	
	/**
	 * Web service to update an existing promo code entity. Each promo code
	 * entity is updated separately and in isolation with conference update.
	 * 
	 * Web service end point: /promo/update/{promoId}
	 * 
	 * Authorization: required
	 * @param promoId
	 * @param NewPromoCode object
	 * @return Confirmation object
	 * @throws EntityNotFoundException
	 */
	@PutMapping(value="/update/{promoId}")
	public Confirmation updateFee(@PathVariable Long promoId, @RequestBody(required=true) NewPromoCode newPromoCode) {
		
		PromotionCodeEntity pce = promoManager.findByPromoId(promoId);
		
		if(pce == null) {
			throw new EntityNotFoundException();
		}
		
		pce.setCode(newPromoCode.getCode());
		pce.setDescription(newPromoCode.getDescription());
		promoManager.createPromoCodeEntity(pce);
		
		return new Confirmation("Promo code Updated", HttpStatus.CREATED.value());
	}
	
	/**
	 * Web service to delete a promo code entity. Unlike Fee entity, promo code entity
	 * does not require to check if any participant has registered with this promo
	 * code or not because it does not violate any foreign ket consistency rules
	 * of databse.
	 * 
	 * Web service end point: /promo/delete/{promoCodeId}
	 * 
	 * Authorization: required
	 * 
	 * @param promoId
	 * @return Confirmation object
	 * @throws EntityNotFoundException
	 */
	@DeleteMapping(value="/delete/{promoCodeId}")
	public Confirmation deletefee(@PathVariable Long promoId) {
		
		PromotionCodeEntity pce = promoManager.findByPromoId(promoId);
		
		if(pce == null) {
			throw new EntityNotFoundException();
		}
			
		promoManager.deleteById(pce.getPromotionCodeId());
		
		return new Confirmation("Promo Code Deleted Successfully", HttpStatus.OK.value());
	}
	
	
}
