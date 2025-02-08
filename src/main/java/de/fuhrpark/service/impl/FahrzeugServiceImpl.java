package de.fuhrpark.service.impl;

import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.persistence.repository.DataStore;
import de.fuhrpark.model.base.Fahrzeug;
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
    public void speichereFahrzeug(Fahrzeug fahrzeug) {
        dataStore.saveFahrzeug(fahrzeug);
    }

    @Override
    public Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen) {
        return dataStore.getFahrzeugByKennzeichen(kennzeichen);
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return dataStore.getAllFahrzeuge();
    }

    @Override
    public void loescheFahrzeug(String kennzeichen) {
        dataStore.deleteFahrzeug(kennzeichen);
    }
} 