package org.registration.service;

import org.registration.persistence.PromotionCodeEntity;
import org.registration.persistence.dao.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value="jpaTransactionManager")
public class PromoManagerImpl implements PromoManager {

	@Autowired
	PromoCodeRepository repository;
	
	@Override
	public void createPromoCodeEntity(PromotionCodeEntity newPromo) {
		
		repository.save(newPromo);
	}

	@Override
	public PromotionCodeEntity findByPromoId(Long promoId) {
		
		return repository.findByPromotionCodeId(promoId);
	}

	@Override
	public void deleteById(Long promoId) {
		
		repository.deleteById(promoId);
	}

}
