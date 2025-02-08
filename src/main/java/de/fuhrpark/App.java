package de.fuhrpark;

import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;

public class App {
    public static void main(String[] args) {
        DataStore dataStore = new DataStoreImpl();
        
        // TODO: Add correct UI initialization once we know the proper class
        System.out.println("Services initialized successfully");
    }
}
