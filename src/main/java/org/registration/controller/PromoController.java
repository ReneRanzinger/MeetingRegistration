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

@RestController
@RequestMapping("/promo")
public class PromoController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	PromoManager promoManager;
	
	@Autowired
	ParticipantManager participantManager;

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
	
	@DeleteMapping(value="/delete/{promoId}")
	public Confirmation deletefee(@PathVariable Long promoId) {
		
		PromotionCodeEntity pce = promoManager.findByPromoId(promoId);
		
		if(pce == null) {
			throw new EntityNotFoundException();
		}
			
		promoManager.deleteById(pce.getPromotionCodeId());
		
		return new Confirmation("Promo Code Deleted Successfully", HttpStatus.OK.value());
	}
	
	
}
