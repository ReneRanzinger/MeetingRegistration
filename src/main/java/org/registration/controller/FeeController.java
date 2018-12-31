package org.registration.controller;

import org.registration.service.ConferenceManager;
import org.registration.service.FeeManager;
import org.registration.view.Confirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fee")
public class FeeController {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	FeeManager feeManager;
	
	@PostMapping(value="/addNew/{conferenceId}")
	public Confirmation addNewFee(@PathVariable Long conferenceId) {
		
		
		return new Confirmation("New Fee Added", HttpStatus.CREATED.value());
	}
	
	@PutMapping(value="/update/{feeId}")
	public Confirmation updateFee(@PathVariable Long feeId) {
		
		
		return new Confirmation("Fee Updated", HttpStatus.CREATED.value());
	}
	
	@DeleteMapping(value="/delete/{feeId}")
	public Confirmation deletefee(@PathVariable Long feeId) {
		
		
		return new Confirmation("Fee Deleted Successfully", HttpStatus.OK.value());
	}
}
