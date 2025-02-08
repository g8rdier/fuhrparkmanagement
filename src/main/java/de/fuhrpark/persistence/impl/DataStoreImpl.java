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
    private Map<String, Fahrzeug> fahrzeuge;
    private Map<String, List<ReparaturBuchEintrag>> reparaturen;
    private Map<String, List<FahrtenbuchEintrag>> fahrten;
    private final String filename;

    public DataStoreImpl(String filename) {
        this.filename = filename;
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            File file = new File(filename);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    @SuppressWarnings("unchecked")
                    Map<String, Fahrzeug> loadedFahrzeuge = (Map<String, Fahrzeug>) ois.readObject();
                    @SuppressWarnings("unchecked")
                    Map<String, List<ReparaturBuchEintrag>> loadedReparaturen = (Map<String, List<ReparaturBuchEintrag>>) ois.readObject();
                    @SuppressWarnings("unchecked")
                    Map<String, List<FahrtenbuchEintrag>> loadedFahrten = (Map<String, List<FahrtenbuchEintrag>>) ois.readObject();
                    
                    this.fahrzeuge = loadedFahrzeuge;
                    this.reparaturen = loadedReparaturen;
                    this.fahrten = loadedFahrten;
                }
            } else {
                this.fahrzeuge = new HashMap<>();
                this.reparaturen = new HashMap<>();
                this.fahrten = new HashMap<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            this.fahrzeuge = new HashMap<>();
            this.reparaturen = new HashMap<>();
            this.fahrten = new HashMap<>();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(fahrzeuge);
            oos.writeObject(reparaturen);
            oos.writeObject(fahrten);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @Override
    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
        saveToFile();
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
        saveToFile();
    }

    @Override
    public void saveFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
        saveToFile();
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        fahrzeuge.remove(kennzeichen);
        saveToFile();
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
        if (!fahrzeuge.containsKey(eintrag.getKennzeichen())) {
            throw new IllegalArgumentException("Fahrzeug nicht gefunden: " + eintrag.getKennzeichen());
        }
        fahrten.computeIfAbsent(eintrag.getKennzeichen(), k -> new ArrayList<>()).add(eintrag);
        saveToFile();
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege() {
        List<FahrtenbuchEintrag> alleEintraege = new ArrayList<>();
        fahrten.values().forEach(alleEintraege::addAll);
        return alleEintraege;
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen) {
        return fahrten.getOrDefault(kennzeichen, new ArrayList<>());
    }

    @Override
    public void addReparaturBuchEintrag(ReparaturBuchEintrag eintrag) {
        String kennzeichen = eintrag.getKennzeichen();
        reparaturen.computeIfAbsent(kennzeichen, k -> new ArrayList<>()).add(eintrag);
        saveToFile();
    }

    @Override
    public void saveReparatur(String kennzeichen, ReparaturBuchEintrag reparatur) {
        reparaturen.computeIfAbsent(kennzeichen, k -> new ArrayList<>()).add(reparatur);
        saveToFile();
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
    public List<ReparaturBuchEintrag> getReparaturen() {
        List<ReparaturBuchEintrag> alleReparaturen = new ArrayList<>();
        reparaturen.values().forEach(alleEintraege -> alleReparaturen.addAll(alleEintraege));
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
        if (!fahrzeuge.containsKey(kennzeichen)) {
            throw new IllegalArgumentException("Fahrzeug nicht gefunden: " + kennzeichen);
        }
        fahrten.computeIfAbsent(kennzeichen, k -> new ArrayList<>()).add(fahrt);
        saveToFile();
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrten(String kennzeichen) {
        return fahrten.getOrDefault(kennzeichen, new ArrayList<>());
    }
}