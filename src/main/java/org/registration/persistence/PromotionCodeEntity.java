package org.registration.persistence;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "promotion_code", schema="registration")
public class PromotionCodeEntity {

	@Id
    @Column(name="promotion_code_id", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq4")
    @SequenceGenerator(name="seq4", sequenceName="promotion_code_seq", initialValue=1, allocationSize=1)
	private Long promotionCodeId;
	
	@ManyToOne
	@JoinColumn(name="conference_id")
	private ConferenceEntity conference;
	
	
	private String code;
	private String description;
	
	public PromotionCodeEntity() {
	
	}

	public PromotionCodeEntity( String code, String description) {
		
		this.code = code;
		this.description = description;
	}

	public Long getPromotionCodeId() {
		return promotionCodeId;
	}

	public void setPromotionCodeId(Long promotionCodeId) {
		this.promotionCodeId = promotionCodeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ConferenceEntity getConference() {
		return conference;
	}

	public void setConference(ConferenceEntity conference) {
		this.conference = conference;
	}
	
	
}
