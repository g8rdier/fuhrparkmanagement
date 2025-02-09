package de.fuhrpark.persistence.repository.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.persistence.repository.DataStore;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileDataStore implements DataStore {
    private static final String DATA_FILE = "fuhrpark_data.ser";
    private Map<String, Fahrzeug> fahrzeuge;

    public FileDataStore() {
        this.fahrzeuge = new HashMap<>();
        load();
    }

    @Override
    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
        save();
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
        save();
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        fahrzeuge.remove(kennzeichen);
        save();
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
    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(fahrzeuge);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Speichern der Daten: " + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            this.fahrzeuge = new HashMap<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            this.fahrzeuge = (Map<String, Fahrzeug>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Fehler beim Laden der Daten: " + e.getMessage(), e);
        }
    }
} 