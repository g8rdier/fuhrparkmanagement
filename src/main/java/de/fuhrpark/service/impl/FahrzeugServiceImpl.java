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
    public void speichereFahrzeug(Fahrzeug fahrzeug) {
        dataStore.speichereFahrzeug(fahrzeug);
    }

    @Override
    public Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen) {
        return dataStore.findeFahrzeugNachKennzeichen(kennzeichen);
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return dataStore.getAlleFahrzeuge();
    }

    @Override
    public void loescheFahrzeug(String kennzeichen) {
        dataStore.loescheFahrzeug(kennzeichen);
    }
} 