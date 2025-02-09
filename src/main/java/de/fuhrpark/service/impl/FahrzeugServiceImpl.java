package de.fuhrpark.service.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.persistence.FahrzeugPersistence;
import java.util.List;

public class FahrzeugServiceImpl {
    private final FahrzeugPersistence persistence;

    public FahrzeugServiceImpl() {
        this.persistence = new FahrzeugPersistence();
    }

    public void saveFahrzeuge(List<Fahrzeug> fahrzeuge) {
        persistence.saveFahrzeuge(fahrzeuge);
    }

    public List<Fahrzeug> loadFahrzeuge() {
        return persistence.loadFahrzeuge();
    }
} 