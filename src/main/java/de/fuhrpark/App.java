package de.fuhrpark;

import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrtenbuchServiceImpl;
import de.fuhrpark.ui.FuhrparkUI;

public class App {
    public static void main(String[] args) {
        DataStore dataStore = new DataStoreImpl();
        FahrzeugService fahrzeugService = new FahrzeugServiceImpl(dataStore);
        FahrtenbuchService fahrtenbuchService = new FahrtenbuchServiceImpl(dataStore);
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            new FuhrparkUI();
        });
    }
}
