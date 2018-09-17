package org.registration.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.registration.persistence.ConfigurationEntity;
import org.registration.persistence.ParticipantEntity;
import org.registration.persistence.dao.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailManageImpl implements EmailManager {

	@Autowired
	ConfigurationRepository configRepository;
	
	@Autowired
	ParticipantManager participantManager;
	
	@Autowired
    private JavaMailSender mailSender;
	
	private String username;
    private String password;
    
    public void init () {
    	if (username == null && password == null) { // load them from db the first time
    	
    		ConfigurationEntity userNameSetting = configRepository.findByName("server.email");
    		ConfigurationEntity passwordSetting = configRepository.findByName("server.email.password");
    		if (userNameSetting != null && passwordSetting != null) {
    			username = userNameSetting.getValue();
    			password = passwordSetting.getValue();
    		
    			((JavaMailSenderImpl)this.mailSender).setPassword(password);
    			((JavaMailSenderImpl)this.mailSender).setUsername(username);
    		} else {
    			throw new RuntimeException("Internal Server Error: email server settings are not in the database");
    		}
    	}
    }
	
	@Override
	public String sendConfirmationEmail(ParticipantEntity participant) {
		
		init(); // if username/password have not been initialized, this will get them from DB
		
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(participant.getEmail()); 
        message.setCc(participant.getConference().getEmailList());
        message.setSubject("[SFG GlyBioInformatics satellite] Registration"); 
        
        Map<String,String> valuesMap = new HashMap<String,String>();
        valuesMap.put("user.firstName", participant.getFirstName());
        valuesMap.put("user.lastName", participant.getLastName());
        valuesMap.put("user.registrationId", participant.getParticipantId().toString());
        
        String templateEmail = participant.getConference().getConfirmationEmail();
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String updatedEmail = sub.replace(templateEmail);
        
        message.setText(updatedEmail);
        message.setFrom(this.username);
        try {
        	mailSender.send(message);
        return updatedEmail;
        } catch (Exception e) {
			return updatedEmail;
		}
	}
	
}
