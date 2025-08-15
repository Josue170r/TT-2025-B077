package com.tt._2025.b077.huellaspormexico.modules.catalogs.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.PostalCodeCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories.PostalCodeRepository;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories.PostalCodeSpecification;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.services.PostalCodeService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostalCodeServiceImpl implements PostalCodeService {

    private final PostalCodeRepository postalCodeRepository;

    public PostalCodeServiceImpl(PostalCodeRepository postalCodeRepository) {
        this.postalCodeRepository = postalCodeRepository;
    }

    @Override
    public List<PostalCodeCatalog> findAll() {
        return postalCodeRepository.findAll();
    }

    @Override
    public List<PostalCodeCatalog> findBySearch(String postalCode, Long settlementId, Long stateId) {
        Specification<PostalCodeCatalog> spec = PostalCodeSpecification.buildSpecification(postalCode, settlementId, stateId);
        Sort sort = Sort.by(Sort.Direction.ASC, "settlement.settlement");
        return postalCodeRepository.findAll(spec, sort);
    }
}
