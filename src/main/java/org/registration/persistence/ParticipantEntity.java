package org.registration.persistence;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="participant", schema = "registration")
public class ParticipantEntity {

	@Id
    @Column(name="participant_id", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq3")
    @SequenceGenerator(name="seq3", sequenceName="participant_seq", initialValue=550011, allocationSize=1)
	private Long participantId;
	
	@ManyToOne(cascade= {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE})
	@JoinColumn(name="conference_id")
	private ConferenceEntity conference;

	@Column(name="title", nullable = false)
	private String title;
	
	@Column(name="first_name", nullable = false)
	private String firstName;
	
	@Column(name="middle_name")
	private String middleName;
	
	@Column(name="last_name", nullable = false)
	private String lastName;
	
	@Column(name="department", nullable = false)
	private String department;
	
	@Column(name="institution", nullable = false)
	private String institution;
	
	@Column(name="email", nullable = false)
	private String email;
	
	@Column(name="address")
	private String address;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="profession", nullable = false)
	private String profession;
	
	@Column(name="promotion_code")
	private String promotionCode;
	
	@OneToOne(cascade= {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE})
	@JoinColumn(name="fee_id")
	private FeeEntity fee;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="registration_time", nullable = false)
	private Timestamp registrationTime;
	
	@Column(name="payed", nullable = false)
	private boolean payed;
	
	@Column(name="abstract_title")
	private String abstractTitle;
	
	@Column(name="abstract")
	private byte[] abstrct;
	
	@Column(name="diet")
	private String diet;
	
	@Column(name="abstract_filename")
	private String abstractFileName;
	
	@Column(name="consider_talk")
	private boolean considerTalk;
	
	public ParticipantEntity() {
		
	}
	
	public ParticipantEntity(String firstName, String middleName, String lastName, String department,
			String institution, String email, String address, String phone, String title, String profession,
			String promotionCode, String comment, Timestamp registrationTime, boolean payed,
			String abstractTitle, byte[] abstrct, String diet, String abstract_fileName, boolean considerTalk) {
		
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.department = department;
		this.institution = institution;
		this.email = email;
		this.address = address;
		this.phone = phone;
		this.title = title;
		this.profession = profession;
		this.promotionCode = promotionCode;
		this.comment = comment;
		this.registrationTime = registrationTime;
		this.payed = payed;
		this.abstractTitle = abstractTitle;
		this.abstrct = abstrct;
		this.diet = diet;
		this.abstractFileName = abstract_fileName;
		this.considerTalk = considerTalk;
	}

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(Timestamp registrationTime) {
		this.registrationTime = registrationTime;
	}

	public boolean isPayed() {
		return payed;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}

	public String getAbstractTitle() {
		return abstractTitle;
	}

	public void setAbstractTitle(String abstractTitle) {
		this.abstractTitle = abstractTitle;
	}

	public byte[] getAbstrct() {
		return abstrct;
	}

	public void setAbstrct(byte[] abstrct) {
		this.abstrct = abstrct;
	}

	public String getDiet() {
		return diet;
	}

	public void setDiet(String diet) {
		this.diet = diet;
	}

	public boolean isConsiderTalk() {
		return considerTalk;
	}

	public void setConsiderTalk(boolean considerTalk) {
		this.considerTalk = considerTalk;
	}

	public ConferenceEntity getConference() {
		return conference;
	}

	public void setConference(ConferenceEntity conference) {
		this.conference = conference;
	}

	public FeeEntity getFee() {
		return fee;
	}

	public void setFee(FeeEntity fee) {
		this.fee = fee;
	}

	public String getAbstractFileName() {
		return abstractFileName;
	}

	public void setAbstractFileName(String abstractFileName) {
		this.abstractFileName = abstractFileName;
	}
	
	
}
