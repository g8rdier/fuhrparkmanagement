// src/main/java/de/fuhrpark/manager/FuhrparkManager.java
package de.fuhrpark.manager;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.persistence.repository.DataStore;

import java.util.List;

/**
 * Zentrale Verwaltungsklasse für den Fuhrpark.
 * Implementiert Dependency Injection für Services.
 */
public class FuhrparkManager {
    private final FahrzeugService fahrzeugService;
    private final FahrzeugFactory fahrzeugFactory;
    private final DataStore dataStore;

    /**
     * Konstruktor mit Dependency Injection
     */
    public FuhrparkManager(FahrzeugService fahrzeugService, FahrzeugFactory fahrzeugFactory, DataStore dataStore) {
        if (fahrzeugService == null || fahrzeugFactory == null) {
            throw new IllegalArgumentException("Services dürfen nicht null sein");
        }
        this.fahrzeugService = fahrzeugService;
        this.fahrzeugFactory = fahrzeugFactory;
        this.dataStore = dataStore;
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public FahrzeugFactory getFahrzeugFactory() {
        return fahrzeugFactory;
    }

    public List<Fahrzeug> getAlleFahrzeuge() {
        return fahrzeugService.getAlleFahrzeuge();
    }

    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeugService.speichereFahrzeug(fahrzeug);
    }

    /**
     * Erstellt und speichert ein neues Fahrzeug
     */
    public Fahrzeug createFahrzeug(String typ, String kennzeichen, String marke, String modell, double preis) {
        FahrzeugTyp fahrzeugTyp = FahrzeugTyp.valueOf(typ);
        Fahrzeug fahrzeug = fahrzeugFactory.erstelleFahrzeug(fahrzeugTyp, marke, modell, kennzeichen, preis);
        fahrzeugService.speichereFahrzeug(fahrzeug);
        return fahrzeug;
    }

    // Overloaded method for backward compatibility with existing test cases
    public Fahrzeug createFahrzeug(String typString, String kennzeichen, String marke, String modell, int sitze, boolean klimaanlage) {
        FahrzeugTyp typ;
        try {
            typ = FahrzeugTyp.valueOf(typString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ungültiger Fahrzeugtyp: " + typString);
        }
        
        // Using a default price of 0.0 for this overload
        Fahrzeug fahrzeug = fahrzeugFactory.erstelleFahrzeug(typ, marke, modell, kennzeichen, 0.0);
        fahrzeugService.speichereFahrzeug(fahrzeug);
        return fahrzeug;
    }

    public void deleteFahrzeug(String kennzeichen) {
        fahrzeugService.loescheFahrzeug(kennzeichen);
    }

    public Fahrzeug findeFahrzeug(String kennzeichen) {
        return fahrzeugService.findeFahrzeugNachKennzeichen(kennzeichen);
    }

    public void updateFahrzeug(Fahrzeug fahrzeug) {
        fahrzeugService.aktualisiereFahrzeug(fahrzeug);
    }
}
