package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.persistence.DataStore;
import java.io.*;  // This includes FileNotFoundException
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStoreImpl implements DataStore {
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
    public void deleteFahrzeug(String kennzeichen) {
        fahrzeuge.remove(kennzeichen);
        fahrtenbuch.remove(kennzeichen);
        reparaturen.remove(kennzeichen);
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
    public void saveReparatur(String kennzeichen, ReparaturBuchEintrag eintrag) {
        reparaturen.computeIfAbsent(kennzeichen, k -> new ArrayList<>()).add(eintrag);
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturBuchEintraege() {
        List<ReparaturBuchEintrag> alleEintraege = new ArrayList<>();
        reparaturen.values().forEach(alleEintraege::addAll);
        return alleEintraege;
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturen(String kennzeichen) {
        return new ArrayList<>(reparaturen.getOrDefault(kennzeichen, new ArrayList<>()));
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturen() {
        List<ReparaturBuchEintrag> alleReparaturen = new ArrayList<>();
        reparaturen.values().forEach(alleReparaturen::addAll);
        return alleReparaturen;
    }

    @Override
    public void save(String filename, Object data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Error saving data to " + filename, e);
        }
    }

    @Override
    public Object load(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // If file doesn't exist yet, return null instead of throwing
            if (e instanceof FileNotFoundException) {
                return null;
            }
            throw new RuntimeException("Error loading data from " + filename, e);
        }
    }

    @Override
    public List<Fahrzeug> getFahrzeuge() {
        return getAlleFahrzeuge();
    }

    @Override
    public void saveFahrt(String kennzeichen, FahrtenbuchEintrag fahrt) {
        List<FahrtenbuchEintrag> fahrten = getFahrten(kennzeichen);
        fahrten.add(fahrt);
        saveToFile();
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrten(String kennzeichen) {
        if (!fahrtenbuch.containsKey(kennzeichen)) {
            return new ArrayList<>();
        }
        FahrzeugData fahrzeugData = fahrtenbuch.get(kennzeichen);
        return fahrzeugData.getFahrten();
    }
}