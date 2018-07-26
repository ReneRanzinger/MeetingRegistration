package org.registration.persistence.dao;

import org.registration.persistence.ConferenceEntity;
import org.registration.persistence.PromotionCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromotionCodeEntity, Long> {

	public PromotionCodeEntity findByCodeAndConferences(String code, ConferenceEntity conference);
	
}
