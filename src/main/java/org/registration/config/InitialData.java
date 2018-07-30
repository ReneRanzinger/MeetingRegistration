package org.registration.config;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.FeeEntity;
import org.registration.service.ConferenceManager;
import org.registration.service.FeeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class InitialData implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	ConferenceManager conferenceManager;
	
	@Autowired
	FeeManager feeManager;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		addAllConferences();
		
	}

private void addAllConferences() {
		
		try {
		ConferenceEntity c1 = new ConferenceEntity();
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
					
		Timestamp registrationStart = new Timestamp(df.parse("06/01/2018").getTime());
		Timestamp registrationEnd = new Timestamp(df.parse("06/30/2018").getTime());
		Timestamp abstractStart = new Timestamp(df.parse("06/15/2018").getTime());
		Timestamp abstractEnd = new Timestamp(df.parse("06/25/2018").getTime());
		
		c1.setConferenceCode("A01");
		c1.setConferenceName("UGA conference 01");
		c1.setRegistrationStart(registrationStart);
		c1.setRegistrationEnd(registrationEnd);
		c1.setAbstractStart(abstractStart);
		c1.setAbstractEnd(abstractEnd);
		c1.setPostRegistrationCode("CONFIRM");
		c1.setConfirmationEmail("Your registration is confirm for UGA conference 01 conference");
		c1.setShortTalks(true);	
		
		conferenceManager.createConference(c1);
		
		ConferenceEntity c2 = new ConferenceEntity();
		
		registrationStart = new Timestamp(df.parse("07/01/2018").getTime());
		registrationEnd = new Timestamp(df.parse("07/30/2018").getTime());
		abstractStart = new Timestamp(df.parse("07/15/2018").getTime());
		abstractEnd = new Timestamp(df.parse("07/25/2018").getTime());
		
		c2.setConferenceCode("A02");
		c2.setConferenceName("UGA conference 02");
		c2.setRegistrationStart(registrationStart);
		c2.setRegistrationEnd(registrationEnd);
		c2.setAbstractStart(abstractStart);
		c2.setAbstractEnd(abstractEnd);
		c2.setPostRegistrationCode("CONFIRM");
		c2.setConfirmationEmail("Your registration is confirm for UGA conference 02 conference");
		c2.setShortTalks(false);	
		
		conferenceManager.createConference(c2);
		
		ConferenceEntity c3 = new ConferenceEntity();
		
		registrationStart = new Timestamp(df.parse("08/01/2018").getTime());
		registrationEnd = new Timestamp(df.parse("08/30/2018").getTime());
		abstractStart = new Timestamp(df.parse("08/15/2018").getTime());
		abstractEnd = new Timestamp(df.parse("08/25/2018").getTime());
		
		c3.setConferenceCode("A03");
		c3.setConferenceName("UGA conference 03");
		c3.setRegistrationStart(registrationStart);
		c3.setRegistrationEnd(registrationEnd);
		c3.setAbstractStart(abstractStart);
		c3.setAbstractEnd(abstractEnd);
		c3.setPostRegistrationCode("CONFIRM");
		c3.setConfirmationEmail("Your registration is confirm for UGA conference 03 conference");
		c3.setShortTalks(false);	
		
		conferenceManager.createConference(c3);
				
		List<FeeEntity> fees = new ArrayList<FeeEntity>();
		
		FeeEntity f1 = new FeeEntity("Standard Fee",1000.0);
		FeeEntity f2 = new FeeEntity("UGA Employee Fee",700.0);
		FeeEntity f3 = new FeeEntity("Federal Employee Fee",500.0);
		
		f1.setConferenceEntity(c1);
		feeManager.createFee(f1);
		f2.setConferenceEntity(c1);
		feeManager.createFee(f2);
		f3.setConferenceEntity(c1);
		feeManager.createFee(f3);
		
		f1 = new FeeEntity("Standard Fee",1000.0);
		f2 = new FeeEntity("GW Employee Fee",700.0);
		f3 = new FeeEntity("State Employee Fee",500.0);
		
		f1.setConferenceEntity(c2);
		feeManager.createFee(f1);
		f2.setConferenceEntity(c2);
		feeManager.createFee(f2);
		f3.setConferenceEntity(c2);
		feeManager.createFee(f3);
		
		f1 = new FeeEntity("Standard Fee",1000.0);
		f2 = new FeeEntity("Retired Employee Fee",700.0);
		f3 = new FeeEntity("Current Employee Fee",500.0);
		
		f1.setConferenceEntity(c3);
		feeManager.createFee(f1);
		f2.setConferenceEntity(c3);
		feeManager.createFee(f2);
		f3.setConferenceEntity(c3);
		feeManager.createFee(f3);
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getNewConferenceCode() {
		
		int leftLimit = 65; // letter 'A'
	    int rightLimit = 90; // letter 'Z'
	    int targetStringLength = 10;
		Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
	 	    
	    return generatedString;
	}
	
}
