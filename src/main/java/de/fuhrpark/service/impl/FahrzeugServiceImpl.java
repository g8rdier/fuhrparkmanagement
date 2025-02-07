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
    public void addFahrzeug(Fahrzeug fahrzeug) {
        dataStore.addFahrzeug(fahrzeug);
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        dataStore.updateFahrzeug(fahrzeug);
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        dataStore.deleteFahrzeug(kennzeichen);
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return dataStore.getAlleFahrzeuge();
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        return dataStore.getFahrzeug(kennzeichen);
    }

    @Override
    public Fahrzeug getFahrzeugByKennzeichen(String kennzeichen) {
        return dataStore.getFahrzeugByKennzeichen(kennzeichen);
    }

    @Override
    public void saveFahrzeug(Fahrzeug fahrzeug) {
        if (getFahrzeugByKennzeichen(fahrzeug.getKennzeichen()) == null) {
            addFahrzeug(fahrzeug);
        } else {
            updateFahrzeug(fahrzeug);
        }
    }
}