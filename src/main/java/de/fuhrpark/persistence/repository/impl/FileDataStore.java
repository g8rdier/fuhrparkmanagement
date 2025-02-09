package de.fuhrpark.persistence.repository.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.persistence.repository.DataStore;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileDataStore<T> {
    private final String filePath;

    public FileDataStore(String filePath) {
        this.filePath = filePath;
    }

    public void save(List<T> items) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(new ArrayList<>(items));
        } catch (IOException e) {
            throw new RuntimeException("Error saving data: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> load() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading data: " + e.getMessage(), e);
        }
    }
} 