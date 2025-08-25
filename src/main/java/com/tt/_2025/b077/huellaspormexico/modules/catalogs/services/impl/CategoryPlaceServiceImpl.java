package com.tt._2025.b077.huellaspormexico.modules.catalogs.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CategoryPlacesCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories.CategoryPlacesRepository;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.services.CategoryPlaceService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryPlaceServiceImpl implements CategoryPlaceService {

    private final CategoryPlacesRepository categoryRepository;

    public CategoryPlaceServiceImpl(CategoryPlacesRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryPlacesCatalog> findAll() {
        return categoryRepository.findAll(Sort.by("category").ascending());
    }
}
