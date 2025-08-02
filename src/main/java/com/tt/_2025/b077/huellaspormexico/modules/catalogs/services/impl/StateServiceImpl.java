package com.tt._2025.b077.huellaspormexico.modules.catalogs.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.StateCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.exceptions.StateNotFound;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories.StateRepository;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.services.StateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public List<StateCatalog> findAll() {
        return stateRepository.findAll();
    }

    @Override
    public StateCatalog findById(Long id) {
        return stateRepository.findById(id).orElseThrow(
                () -> new StateNotFound("Estado no encontrado: " +id)
        );
    }

    @Override
    public StateCatalog findByState(String state) {
        return stateRepository.findByState(state).orElseThrow(
                () -> new StateNotFound("Estado no encontrado: " +state)
        );
    }
}
