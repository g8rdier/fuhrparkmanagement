package de.fuhrpark.service.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.persistence.DataStore;
import java.util.List;

/**
 * Implementierung des FahrzeugService
 */
public class FahrzeugServiceImpl implements FahrzeugService {
    private final DataStore dataStore;

    public FahrzeugServiceImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void speichereFahrzeug(Fahrzeug fahrzeug) {
        dataStore.saveFahrzeug(fahrzeug);
    }

    @Override
    public void aktualisiereFahrzeug(Fahrzeug fahrzeug) {
        dataStore.updateFahrzeug(fahrzeug);
    }

    @Override
    public void loescheFahrzeug(String kennzeichen) {
        dataStore.deleteFahrzeug(kennzeichen);
    }

    @Override
    public Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen) {
        return dataStore.getFahrzeugByKennzeichen(kennzeichen);
    }

    @Override
    public List<Fahrzeug> findeAlleFahrzeuge() {
        return dataStore.getAlleFahrzeuge();
    }
}