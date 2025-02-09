package de.fuhrpark.persistence;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.persistence.repository.impl.FileDataStore;
import java.util.List;

public class FahrzeugPersistence {
    private static final String FILE_PATH = System.getProperty("user.home") 
        + System.getProperty("file.separator") 
        + "fuhrpark-data.ser";
        
    private final FileDataStore<Fahrzeug> dataStore;

    public FahrzeugPersistence() {
        this.dataStore = new FileDataStore<>(FILE_PATH);
    }

    public void saveFahrzeuge(List<Fahrzeug> fahrzeuge) {
        dataStore.save(fahrzeuge);
    }

    public List<Fahrzeug> loadFahrzeuge() {
        return dataStore.load();
    }
} 