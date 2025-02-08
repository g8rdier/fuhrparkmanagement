// src/main/java/de/fuhrpark/manager/FuhrparkManager.java
package de.fuhrpark.manager;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.base.FahrzeugFactory;

/**
 * Zentrale Verwaltungsklasse für den Fuhrpark.
 * Implementiert Dependency Injection für Services.
 */
public class FuhrparkManager {
    private final FahrzeugService fahrzeugService;
    private final FahrzeugFactory fahrzeugFactory;

    /**
     * Konstruktor mit Dependency Injection
     */
    public FuhrparkManager(FahrzeugService fahrzeugService, FahrzeugFactory fahrzeugFactory) {
        this.fahrzeugService = fahrzeugService;
        this.fahrzeugFactory = fahrzeugFactory;
    }

    /**
     * Erstellt und speichert ein neues Fahrzeug
     */
    public Fahrzeug createFahrzeug(String typString, String kennzeichen, String marke, String modell, double preis) {
        FahrzeugTyp typ;
        try {
            typ = FahrzeugTyp.valueOf(typString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ungültiger Fahrzeugtyp: " + typString);
        }
        
        Fahrzeug fahrzeug = fahrzeugFactory.erstelleFahrzeug(typ, marke, modell, kennzeichen, preis);
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
