package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.persistence.DataStore;

import java.util.*;

public class DataStoreImpl implements DataStore {
    private final Map<String, Object> storage;
    private final Map<String, Fahrzeug> fahrzeuge = new HashMap<>();
    private final Map<String, List<FahrtenbuchEintrag>> fahrtenbuch = new HashMap<>();
    private final Map<String, List<ReparaturBuchEintrag>> reparaturen = new HashMap<>();

    public DataStoreImpl() {
        this.storage = new HashMap<>();
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
    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        if (fahrzeuge.containsKey(fahrzeug.getKennzeichen())) {
            fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
        }
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        fahrzeuge.remove(kennzeichen);
        fahrtenbuch.remove(kennzeichen);
        reparaturen.remove(kennzeichen);
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return new ArrayList<>(fahrzeuge.values());
    }

    @Override
    public void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag) {
        fahrtenbuch.computeIfAbsent(eintrag.getKennzeichen(), _ -> new ArrayList<>()).add(eintrag);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen) {
        return fahrtenbuch.getOrDefault(kennzeichen, new ArrayList<>());
    }

    @Override
    public void addReparatur(ReparaturBuchEintrag reparatur) {
        reparaturen.computeIfAbsent(reparatur.getKennzeichen(), _ -> new ArrayList<>()).add(reparatur);
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturen(String kennzeichen) {
        return reparaturen.getOrDefault(kennzeichen, new ArrayList<>());
    }
} 