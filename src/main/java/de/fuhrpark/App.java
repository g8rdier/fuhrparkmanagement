package de.fuhrpark;

import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;
import de.fuhrpark.ui.FuhrparkUI;

public class App {
    private final FuhrparkManager manager;

    public App() {
        DataStore dataStore = new DataStoreImpl();
        this.manager = new FuhrparkManager(dataStore);
    }

    public void start() {
        // Create and show the main UI
        javax.swing.SwingUtilities.invokeLater(() -> {
            FuhrparkUI ui = new FuhrparkUI();
            ui.setVisible(true);
        });
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }
}
