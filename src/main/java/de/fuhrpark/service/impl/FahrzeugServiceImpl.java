package de.fuhrpark.service.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.persistence.DataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FahrzeugServiceImpl implements FahrzeugService {
    private final Map<String, Fahrzeug> fahrzeuge = new HashMap<>();
    private final DataStore dataStore;

    public FahrzeugServiceImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void addFahrzeug(Fahrzeug fahrzeug) {
        if (fahrzeug == null) {
            throw new IllegalArgumentException("Fahrzeug darf nicht null sein");
        }
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return new ArrayList<>(fahrzeuge.values());
    }

    @Override
    public Fahrzeug getFahrzeugByKennzeichen(String kennzeichen) {
        return fahrzeuge.get(kennzeichen);
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        if (fahrzeug == null || !fahrzeuge.containsKey(fahrzeug.getKennzeichen())) {
            throw new IllegalArgumentException("Fahrzeug existiert nicht");
        }
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        fahrzeuge.remove(kennzeichen);
    }
} 