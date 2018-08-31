package org.registration.persistence;

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
@Table(name = "fee", schema = "registration")
public class FeeEntity {

	@Id
    @Column(name="fee_id", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq2")
    @SequenceGenerator(name="seq2", sequenceName="fee_seq", initialValue=1, allocationSize=1)
	private Long feeId;
	
	@ManyToOne(cascade= {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE})
	@JoinColumn(name="conference_id")
	private ConferenceEntity conferenceEntity;
	
	@Column(name= "name", nullable = false)
	private String name;
	
	@Column(name="amount", nullable = false)
	private double amount;
	
	public FeeEntity() {
		
	}
	
	public FeeEntity(String name, double amount) {
		this.name = name;
		this.amount = amount;
	}

	public FeeEntity(ConferenceEntity conferenceEntity, String name, double amount) {
		this.conferenceEntity = conferenceEntity;
		this.name = name;
		this.amount = amount;
	}

	public Long getFeeId() {
		return feeId;
	}

	public void setFeeId(Long feeId) {
		this.feeId = feeId;
	}

	public ConferenceEntity getConferenceEntity() {
		return conferenceEntity;
	}

	public void setConferenceEntity(ConferenceEntity conferenceEntity) {
		this.conferenceEntity = conferenceEntity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
