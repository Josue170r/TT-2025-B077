package com.tt._2025.b077.huellaspormexico.modules.catalogs.services;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.StateCatalog;

import java.util.List;

public interface StateService {
    List<StateCatalog> findAll();
    StateCatalog findById(Long id);
    StateCatalog findByState(String state);
}
