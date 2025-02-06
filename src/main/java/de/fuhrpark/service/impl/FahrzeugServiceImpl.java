package de.fuhrpark.service.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.service.FahrzeugService;
import java.util.List;

public class FahrzeugServiceImpl implements FahrzeugService {
    private final DataStore dataStore;

    public FahrzeugServiceImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return dataStore.getAlleFahrzeuge();
    }

    @Override
    public void addFahrzeug(Fahrzeug fahrzeug) {
        dataStore.saveFahrzeug(fahrzeug);
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        dataStore.saveFahrzeug(fahrzeug);
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        dataStore.deleteFahrzeug(kennzeichen);
    }
} 