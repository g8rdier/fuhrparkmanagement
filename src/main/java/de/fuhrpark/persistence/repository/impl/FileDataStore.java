package de.fuhrpark.persistence.repository.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.persistence.repository.DataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.nio.file.*;

public class FileDataStore implements DataStore {
    private final Map<String, Fahrzeug> fahrzeuge;
    private final Map<String, List<FahrtenbuchEintrag>> fahrtenbuecher;
    private static final String DATA_FILE = "fuhrpark_data.ser";

    public FileDataStore() {
        this.fahrzeuge = new HashMap<>();
        this.fahrtenbuecher = new HashMap<>();
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(fahrzeuge);
            oos.writeObject(fahrtenbuecher);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void load() {
        if (!Files.exists(Paths.get(DATA_FILE))) {
            return; // No file to load yet
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            @SuppressWarnings("unchecked")
            Map<String, Fahrzeug> loadedFahrzeuge = 
                (Map<String, Fahrzeug>) ois.readObject();
            @SuppressWarnings("unchecked")
            Map<String, List<FahrtenbuchEintrag>> loadedFahrtenbuecher = 
                (Map<String, List<FahrtenbuchEintrag>>) ois.readObject();
            
            fahrzeuge.clear();
            fahrzeuge.putAll(loadedFahrzeuge);
            fahrtenbuecher.clear();
            fahrtenbuecher.putAll(loadedFahrtenbuecher);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
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