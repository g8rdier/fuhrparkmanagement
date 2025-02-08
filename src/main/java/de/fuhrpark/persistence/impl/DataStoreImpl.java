package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.persistence.repository.DataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-Memory Implementierung des DataStore
 */
public class DataStoreImpl implements DataStore {
    private final Map<String, Fahrzeug> fahrzeuge = new HashMap<>();

    @Override
    public void saveFahrzeug(Fahrzeug fahrzeug) {
        if (fahrzeug == null || fahrzeug.getKennzeichen() == null) {
            throw new IllegalArgumentException("Fahrzeug und Kennzeichen dürfen nicht null sein");
        }
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        if (fahrzeug == null || fahrzeug.getKennzeichen() == null) {
            throw new IllegalArgumentException("Fahrzeug und Kennzeichen dürfen nicht null sein");
        }
        if (!fahrzeuge.containsKey(fahrzeug.getKennzeichen())) {
            throw new IllegalStateException("Fahrzeug nicht gefunden: " + fahrzeug.getKennzeichen());
        }
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        fahrzeuge.remove(kennzeichen);
    }

    @Override
    public Fahrzeug getFahrzeugByKennzeichen(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        return fahrzeuge.get(kennzeichen);
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return new ArrayList<>(fahrzeuge.values());
    }

    @Override
    public void saveFahrt(String kennzeichen, FahrtenbuchEintrag eintrag) {
        // For BANF0-2, we'll implement a simple version that just logs
        System.out.println("DataStoreImpl: Would save fahrt for " + kennzeichen);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrten(String kennzeichen) {
        // For BANF0-2, return empty list
        return new ArrayList<FahrtenbuchEintrag>();
    }
}