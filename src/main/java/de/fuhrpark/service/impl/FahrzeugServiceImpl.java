package de.fuhrpark.service.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.persistence.repository.DataStore;
import de.fuhrpark.service.base.FahrzeugService;
import java.util.List;

public class FahrzeugServiceImpl implements FahrzeugService {
    private final DataStore dataStore;

    public FahrzeugServiceImpl(DataStore dataStore) {
        if (dataStore == null) {
            throw new IllegalArgumentException("DataStore darf nicht null sein");
        }
        this.dataStore = dataStore;
    }

    @Override
    public void addFahrzeug(Fahrzeug fahrzeug) {
        dataStore.addFahrzeug(fahrzeug);
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        dataStore.updateFahrzeug(fahrzeug);
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        return dataStore.getFahrzeug(kennzeichen);
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return dataStore.getAlleFahrzeuge();
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        dataStore.deleteFahrzeug(kennzeichen);
    }
} 