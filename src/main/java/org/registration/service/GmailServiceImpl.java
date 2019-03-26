package org.registration.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.registration.persistence.ConfigurationEntity;
import org.registration.persistence.ParticipantEntity;
import org.registration.persistence.dao.ConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

@Service
public final class GmailServiceImpl implements EmailManager {

	Logger logger = LoggerFactory.getLogger(EmailManageImpl.class);

	@Autowired
	ConfigurationRepository configRepository;

	@Autowired
	ParticipantManager participantManager;

	private String username;
	private String password;

	@Value("${jasypt.encryptor.password}")
	private String JASYPT_SECRET;

	private final NetHttpTransport httpTransport;

	@Value("${gmail.client-id}")
	private String clientId;

	@Value("${gmail.client-secret}")
	private String clientSecret;

	@Value("${gmail.access-token}")
	private String accessToken;

	@Value("${gmail.refresh-token}")
	private String refreshToken;
	
	private static final String APPLICATION_NAME = "ConferenceRegistration";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	public GmailServiceImpl() {
		try {
			this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void init() {
		if (username == null && password == null) { // load them from db the first time

			ConfigurationEntity userNameSetting = configRepository.findByName("server.email");
			ConfigurationEntity passwordSetting = configRepository.findByName("server.email.password");
			if (userNameSetting != null && passwordSetting != null) {
				username = userNameSetting.getValue();
				password = passwordSetting.getValue();
				StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
				decryptor.setPassword(JASYPT_SECRET);
				password = decryptor.decrypt(password);
			} else {
				throw new RuntimeException("Internal Server Error: email server settings are not in the database");
			}
		}
	}

	public boolean sendMessage(String recipientAddress, String ccAddresses, String subject, String body)
			throws MessagingException, IOException {
		Message message = createMessageWithEmail(createEmail(recipientAddress, ccAddresses, username, subject, body));

		return createGmail().users().messages().send(username, message).execute().getLabelIds().contains("SENT");
	}

	private Gmail createGmail() {
		Credential credential = authorize();
		return new Gmail.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	private MimeMessage createEmail(String to, String cc, String from, String subject, String bodyText) throws MessagingException {
		MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		
		for(String acc : cc.split(";")) {
			email.addRecipient(RecipientType.CC, new InternetAddress(acc));
		}		
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}

	private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		emailContent.writeTo(buffer);

		return new Message().setRaw(Base64.encodeBase64URLSafeString(buffer.toByteArray()));
	}

	private Credential authorize() {
		return new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(JSON_FACTORY)
				.setClientSecrets(clientId, clientSecret).build().setAccessToken(accessToken)
				.setRefreshToken(refreshToken);
	}

	@Override
	public String sendConfirmationEmail(ParticipantEntity participant) {
		init(); // if username/password have not been initialized, this will get them from DB
        
        Map<String,String> valuesMap = new HashMap<String,String>();
        valuesMap.put("user.firstName", participant.getFirstName());
        valuesMap.put("user.lastName", participant.getLastName());
        valuesMap.put("user.registrationId", participant.getParticipantId().toString());
        
        String templateEmail = participant.getConference().getConfirmationEmail();
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String updatedEmail = sub.replace(templateEmail);
        
        try {
        	sendMessage(participant.getEmail(), participant.getConference().getEmailList(), 
        			participant.getConference().getConferenceName() + " Registration Confirmation", updatedEmail);
        	return updatedEmail;
        } catch (Exception e) {
        	logger.error("Error sending email", e);
			return updatedEmail;
		}
	}

}
