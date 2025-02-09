package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.base.Fahrzeug;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-Memory Implementierung des DataStore
 */
public class DatabaseDataStoreImpl {
    private final Map<String, Fahrzeug> fahrzeuge = new HashMap<>();
    private final Map<String, List<FahrtenbuchEintrag>> fahrtenbuch = new HashMap<>();

    public void speichereFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    public void aktualisiereFahrzeug(Fahrzeug fahrzeug) {
        if (fahrzeug == null || fahrzeug.getKennzeichen() == null) {
            throw new IllegalArgumentException("Fahrzeug und Kennzeichen dürfen nicht null sein");
        }
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    public void loescheFahrzeug(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        fahrzeuge.remove(kennzeichen);
        fahrtenbuch.remove(kennzeichen);
    }

    public Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        return fahrzeuge.get(kennzeichen);
    }

    public List<Fahrzeug> getAlleFahrzeuge() {
        return new ArrayList<>(fahrzeuge.values());
    }

    public void addFahrtenbuchEintrag(String kennzeichen, FahrtenbuchEintrag eintrag) {
        if (kennzeichen == null || eintrag == null) {
            throw new IllegalArgumentException("Kennzeichen und Eintrag dürfen nicht null sein");
        }
        fahrtenbuch.computeIfAbsent(kennzeichen, k -> new ArrayList<>()).add(eintrag);
    }

    public List<FahrtenbuchEintrag> getFahrtenForFahrzeug(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        return fahrtenbuch.getOrDefault(kennzeichen, new ArrayList<>());
    }
}