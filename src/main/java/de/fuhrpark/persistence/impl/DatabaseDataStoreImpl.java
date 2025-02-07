package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.persistence.DataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseDataStoreImpl implements DataStore {
    private final Map<String, Fahrzeug> fahrzeuge = new HashMap<>();
    private final Map<String, List<FahrtenbuchEintrag>> fahrtenbuch = new HashMap<>();
    private final Map<String, List<ReparaturBuchEintrag>> reparaturen = new HashMap<>();

    @Override
    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public void saveFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        return fahrzeuge.get(kennzeichen);
    }

    @Override
    public List<Fahrzeug> getFahrzeuge() {
        return new ArrayList<>(fahrzeuge.values());
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
    public void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag) {
        fahrtenbuch.computeIfAbsent(eintrag.getKennzeichen(), k -> new ArrayList<>())
                  .add(eintrag);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege() {
        List<FahrtenbuchEintrag> alleEintraege = new ArrayList<>();
        fahrtenbuch.values().forEach(alleEintraege::addAll);
        return alleEintraege;
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen) {
        return fahrtenbuch.getOrDefault(kennzeichen, new ArrayList<>());
    }

    @Override
    public void addReparaturBuchEintrag(ReparaturBuchEintrag eintrag) {
        reparaturen.computeIfAbsent(eintrag.getKennzeichen(), k -> new ArrayList<>())
                  .add(eintrag);
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturBuchEintraege() {
        List<ReparaturBuchEintrag> alleEintraege = new ArrayList<>();
        reparaturen.values().forEach(alleEintraege::addAll);
        return alleEintraege;
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturen(String kennzeichen) {
        return reparaturen.getOrDefault(kennzeichen, new ArrayList<>());
    }

    @Override
    public void save(String filename, Object data) {
        // TODO: Implement persistence
    }

    @Override
    public Object load(String filename) {
        // TODO: Implement loading
        return null;
    }
}