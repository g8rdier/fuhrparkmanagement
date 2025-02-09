package de.fuhrpark.persistence.repository.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.persistence.repository.DataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileDataStore implements DataStore {
    private final Map<String, Fahrzeug> fahrzeuge;
    private final Map<String, List<FahrtenbuchEintrag>> fahrtenbuecher;

    public FileDataStore() {
        this.fahrzeuge = new HashMap<>();
        this.fahrtenbuecher = new HashMap<>();
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
    public void aktualisiereFahrzeug(Fahrzeug fahrzeug) {
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

    @Override
    public List<FahrtenbuchEintrag> getFahrtenForFahrzeug(String kennzeichen) {
        return fahrtenbuecher.getOrDefault(kennzeichen, new ArrayList<>());
    }

    @Override
    public void addFahrtenbuchEintrag(String kennzeichen, FahrtenbuchEintrag eintrag) {
        fahrtenbuecher.computeIfAbsent(kennzeichen, k -> new ArrayList<>()).add(eintrag);
    }
} 