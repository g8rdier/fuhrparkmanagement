package de.fuhrpark.persistence.repository.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.persistence.repository.DataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileDataStore implements DataStore {
    private final Map<String, Fahrzeug> fahrzeuge;

    public FileDataStore() {
        this.fahrzeuge = new HashMap<>();
    }

    @Override
    public void save() {
        // TODO: Implement actual file saving
    }

    @Override
    public void load() {
        // TODO: Implement actual file loading
    }

    @Override
    public void speichereFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen) {
        return fahrzeuge.get(kennzeichen);
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return new ArrayList<>(fahrzeuge.values());
    }

    @Override
    public void loescheFahrzeug(String kennzeichen) {
        fahrzeuge.remove(kennzeichen);
    }
} 