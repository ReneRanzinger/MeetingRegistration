package org.registration.persistence;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="conference", schema="registration")
public class ConferenceEntity {

	@Id
    @Column(name="conference_id", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq1")
    @SequenceGenerator(name="seq1", sequenceName="conference_seq", initialValue=1)
	private Long conferenceId;
	
	@Column(name="conference_code", unique = true, nullable = false, length = 31)
	private String conferenceCode;
	
	@Column(name="conference_name", nullable = false, length = 255)
	private String conferenceName;
	
	@Column(name="registratoin_start", nullable = false)
	private Timestamp registrationStart;
	
	@Column(name="registration_end", nullable = false)
	private Timestamp registrationEnd;
	
	@Column(name="abstract_start", nullable = false)
	private Timestamp abstractStart;
	
	@Column(name="abstract_end", nullable = false)
	private Timestamp abstractEnd;
	
	@Column(name="post_registration_code", nullable = false, length = 31)
	private String postRegistrationCode;
	
	@Column(name="confirmation_email", nullable = false)
	private String confirmationEmail;
	
	@Column(name="short_talks")
	private boolean shortTalks;
	
	@OneToMany(mappedBy="conferenceEntity", cascade= {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE})
	private List<FeeEntity> feeEntities;	
	
	@OneToMany(mappedBy = "conference", cascade= {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE})	
	private Collection<ParticipantEntity> participants;
	
	@ManyToMany
	@JoinTable(name="conference_promocode", schema = "registration", 
					joinColumns = @JoinColumn(name="conference_id"),
					inverseJoinColumns = @JoinColumn(name="promo_code_id"))
	private Collection<PromotionCodeEntity> promoCodes;
	
	
	public ConferenceEntity() {
		
	}
	
	public ConferenceEntity(String conferenceCode, String conferenceName, Timestamp registrationStart,
			Timestamp registrationEnd, Timestamp abstractStart, Timestamp abstractEnd, String postRegistrationCode,
			String confirmationEmail, boolean shortTalks) {
		super();
		this.conferenceCode = conferenceCode;
		this.conferenceName = conferenceName;
		this.registrationStart = registrationStart;
		this.registrationEnd = registrationEnd;
		this.abstractStart = abstractStart;
		this.abstractEnd = abstractEnd;
		this.postRegistrationCode = postRegistrationCode;
		this.confirmationEmail = confirmationEmail;
		this.shortTalks = shortTalks;
	}

	public Long getConferenceId() {
		return conferenceId;
	}
	public void setConferenceId(Long conferenceId) {
		this.conferenceId = conferenceId;
	}
	public String getConferenceCode() {
		return conferenceCode;
	}
	public void setConferenceCode(String conferenceCode) {
		this.conferenceCode = conferenceCode;
	}
	public String getConferenceName() {
		return conferenceName;
	}
	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}
	public Timestamp getRegistrationStart() {
		return registrationStart;
	}
	public void setRegistrationStart(Timestamp registrationStart) {
		this.registrationStart = registrationStart;
	}
	public Timestamp getRegistrationEnd() {
		return registrationEnd;
	}
	public void setRegistrationEnd(Timestamp registrationEnd) {
		this.registrationEnd = registrationEnd;
	}
	public Timestamp getAbstractStart() {
		return abstractStart;
	}
	public void setAbstractStart(Timestamp abstractStart) {
		this.abstractStart = abstractStart;
	}
	public Timestamp getAbstractEnd() {
		return abstractEnd;
	}
	public void setAbstractEnd(Timestamp abstractEnd) {
		this.abstractEnd = abstractEnd;
	}
	public String getPostRegistrationCode() {
		return postRegistrationCode;
	}
	public void setPostRegistrationCode(String postRegistrationCode) {
		this.postRegistrationCode = postRegistrationCode;
	}
	public String getConfirmationEmail() {
		return confirmationEmail;
	}
	public void setConfirmationEmail(String confirmationEmail) {
		this.confirmationEmail = confirmationEmail;
	}
	public boolean isShortTalks() {
		return shortTalks;
	}
	public void setShortTalks(boolean shortTalks) {
		this.shortTalks = shortTalks;
	}

	public List<FeeEntity> getFeeEntities() {
		return feeEntities;
	}

	public void setFeeEntities(List<FeeEntity> feeEntities) {
		this.feeEntities = feeEntities;
	}

	public Collection<ParticipantEntity> getParticipants() {
		return participants;
	}

	public void setParticipants(Collection<ParticipantEntity> participants) {
		this.participants = participants;
	}

	public Collection<PromotionCodeEntity> getPromoCodes() {
		return promoCodes;
	}

	public void setPromoCodes(Collection<PromotionCodeEntity> promoCodes) {
		this.promoCodes = promoCodes;
	}
	
}
