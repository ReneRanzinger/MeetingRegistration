package org.registration.config;

import java.sql.Timestamp;
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
		boolean firstTime=true;
		try {
		ConferenceEntity c1 = new ConferenceEntity();
		
		if(conferenceManager.findByConferenceCode("XABKSHRKZU")!=null) {
			firstTime=false;
		}
		
		if(firstTime) {						
			Timestamp registrationStart = java.sql.Timestamp.valueOf("2018-09-01 00:00:00");
			Timestamp registrationEnd = java.sql.Timestamp.valueOf("2018-11-05 23:59:59");
			Timestamp abstractStart = java.sql.Timestamp.valueOf("2018-09-01 00:00:00");
			Timestamp abstractEnd = java.sql.Timestamp.valueOf("2018-09-01 23:59:59");
			
			c1.setConferenceCode("XABKSHRKZU");
			c1.setConferenceName("SFG - GlycoBioInformatics satellite meeting");
			c1.setRegistrationStart(registrationStart);
			c1.setRegistrationEnd(registrationEnd);
			c1.setAbstractStart(abstractStart);
			c1.setAbstractEnd(abstractEnd);
			c1.setPostRegistrationCode("JSIWKCGAUR");
			c1.setEmailList("rene@ccrc.uga.edu");
			c1.setConfirmationEmail("Dear ${user.firstName} ${user.lastName},"
					+ "\n\n"
					+ "Thank you for registering for the GlycoBioInformatics satellite meeting "
					+ "of the annual SFG meeting 2018, which will be held November 5, 2018, in "
					+ "New Orleans, Louisiana. Your registration confirmation number is "
					+ "${user.registrationId}."
					+ "\n\n"
					+ "The program for the satellite can be found under: "
					+ "http://glycomics.ccrc.uga.edu/meetings/sfg/GlycoInformaticsSatelliteProgram.pdf. "
					+ "The meeting will have 3 sessions: "
					+ "(1) Databases and information standards; "
					+ "(2) Processing and annotating analytical data; and "
					+ "(3) Tools. In these sessions database providers and software developers will "
					+ "introduce their software tools to the audience in short talks followed by a few "
					+ "questions. Later in the afternoon all developers will present their tool in live "
					+ "demonstrations to the audience."
					+ "\n\n"
					+ "We look forward to your participation in what we feel will be a very "
					+ "exciting meeting. Please do not hesitate to contact René Ranzinger "
					+ "(rene@ccrc.uga.edu) if you need additional assistance, or if you have "
					+ "any questions or concerns."
					+ "\n\n"
					+ "Yours sincerely,"
					+ "\n"
					+ " René Ranzinger"
					+ "\n\n"
					+ "Please note: The registration for the satellite does not entitle you to attend the SFG main meeting. "
					+ "If you wish to attend this meeting as well please make sure to register at the meeting website "
					+ "(http://glycobiology.org/Meetings.aspx).");
			
			c1.setShortTalks(false);	
			
			conferenceManager.createConference(c1);
										
			FeeEntity f1 = new FeeEntity("Standard Fee",0.0);
			
			f1.setConferenceEntity(c1);
			feeManager.createFee(f1);
			
			}
		} catch (Exception e) {
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
