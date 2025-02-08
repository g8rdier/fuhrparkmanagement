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
    public void saveFahrzeug(Fahrzeug fahrzeug) {
        System.out.println("FahrzeugService: Saving vehicle " + fahrzeug.getKennzeichen());
        dataStore.saveFahrzeug(fahrzeug);
        System.out.println("FahrzeugService: Vehicle saved successfully");
    }

    @Override
    public void addFahrzeug(Fahrzeug fahrzeug) {
        saveFahrzeug(fahrzeug); // Delegate to saveFahrzeug
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        saveFahrzeug(fahrzeug); // Delegate to saveFahrzeug
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        return dataStore.getFahrzeug(kennzeichen);
    }

    @Override
    public Fahrzeug getFahrzeugByKennzeichen(String kennzeichen) {
        return getFahrzeug(kennzeichen); // Delegate to getFahrzeug
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        List<Fahrzeug> fahrzeuge = dataStore.getAlleFahrzeuge();
        System.out.println("FahrzeugService: Retrieved " + fahrzeuge.size() + " vehicles");
        return fahrzeuge;
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        dataStore.deleteFahrzeug(kennzeichen);
    }
}