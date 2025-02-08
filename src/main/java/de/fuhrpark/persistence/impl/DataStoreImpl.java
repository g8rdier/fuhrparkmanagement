package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.persistence.DataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStoreImpl implements DataStore {
    private final Map<String, Fahrzeug> fahrzeuge = new HashMap<>();

    @Override
    public void saveFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        return fahrzeuge.get(kennzeichen);
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return new ArrayList<>(fahrzeuge.values());
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        fahrzeuge.remove(kennzeichen);
    }
}