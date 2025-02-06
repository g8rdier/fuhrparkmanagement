package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.persistence.DataStore;

import java.util.*;

public class DataStoreImpl implements DataStore {
    private final Map<String, Object> storage;
    private final Map<String, Fahrzeug> fahrzeuge;
    private final Map<String, List<FahrtenbuchEintrag>> fahrtenbuch;
    private final Map<String, List<ReparaturBuchEintrag>> reparaturen;

    public DataStoreImpl() {
        this.storage = new HashMap<>();
        this.fahrzeuge = new HashMap<>();
        this.fahrtenbuch = new HashMap<>();
        this.reparaturen = new HashMap<>();
    }

    @Override
    public void save(String key, Object data) {
        storage.put(key, data);
    }

    @Override
    public Optional<Object> load(String key) {
        return Optional.ofNullable(storage.get(key));
    }

    @Override
    public void delete(String key) {
        storage.remove(key);
    }

    @Override
    public void saveFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        fahrzeuge.remove(kennzeichen);
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return new ArrayList<>(fahrzeuge.values());
    }

    @Override
    public void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag) {
        fahrtenbuch.computeIfAbsent(eintrag.getFahrzeugKennzeichen(), k -> new ArrayList<>())
                  .add(eintrag);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen) {
        return fahrtenbuch.getOrDefault(kennzeichen, new ArrayList<>());
    }

    @Override
    public void addReparatur(ReparaturBuchEintrag reparatur) {
        reparaturen.computeIfAbsent(reparatur.getFahrzeugKennzeichen(), k -> new ArrayList<>())
                  .add(reparatur);
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturen(String kennzeichen) {
        return reparaturen.getOrDefault(kennzeichen, new ArrayList<>());
    }
} 