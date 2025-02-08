package de.fuhrpark.service.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.persistence.repository.DataStore;
import java.util.List;

/**
 * Implementierung des FahrzeugService
 */
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
        if (fahrzeug == null) {
            throw new IllegalArgumentException("Fahrzeug darf nicht null sein");
        }
        dataStore.saveFahrzeug(fahrzeug);
    }

    @Override
    public void aktualisiereFahrzeug(Fahrzeug fahrzeug) {
        if (fahrzeug == null) {
            throw new IllegalArgumentException("Fahrzeug darf nicht null sein");
        }
        dataStore.updateFahrzeug(fahrzeug);
    }

    @Override
    public void loescheFahrzeug(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        dataStore.deleteFahrzeug(kennzeichen);
    }

    @Override
    public Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        return dataStore.getFahrzeugByKennzeichen(kennzeichen);
    }

    @Override
    public List<Fahrzeug> findeAlleFahrzeuge() {
        return dataStore.getAlleFahrzeuge();
    }
}