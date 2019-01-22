package org.registration.service;

import org.registration.persistence.PromotionCodeEntity;

public interface PromoManager {

	public void createPromoCodeEntity(PromotionCodeEntity newPromo);
	public PromotionCodeEntity findByPromoId(Long promoId);
	public void deleteById(Long promoId);
	
}
