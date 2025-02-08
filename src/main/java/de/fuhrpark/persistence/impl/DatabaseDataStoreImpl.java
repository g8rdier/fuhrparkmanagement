package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.impl.PKW;
import de.fuhrpark.model.impl.LKW;
import de.fuhrpark.persistence.repository.DataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-Memory Implementierung des DataStore
 */
public class DatabaseDataStoreImpl implements DataStore {
    private final Map<String, Fahrzeug> fahrzeuge = new HashMap<>();
    private final Map<String, List<FahrtenbuchEintrag>> fahrtenbuch = new HashMap<>();

    @Override
    public void speichereFahrzeug(Fahrzeug fahrzeug) {
        if (fahrzeug == null || fahrzeug.getKennzeichen() == null) {
            throw new IllegalArgumentException("Fahrzeug und Kennzeichen dürfen nicht null sein");
        }
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public void loescheFahrzeug(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        fahrzeuge.remove(kennzeichen);
        fahrtenbuch.remove(kennzeichen);
    }

    @Override
    public Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen) {
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
    public void addFahrtenbuchEintrag(String kennzeichen, FahrtenbuchEintrag eintrag) {
        if (kennzeichen == null || eintrag == null) {
            throw new IllegalArgumentException("Kennzeichen und Eintrag dürfen nicht null sein");
        }
        fahrtenbuch.computeIfAbsent(kennzeichen, k -> new ArrayList<>()).add(eintrag);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenForFahrzeug(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        return fahrtenbuch.getOrDefault(kennzeichen, new ArrayList<>());
    }
}