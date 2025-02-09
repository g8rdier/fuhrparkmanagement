// src/main/java/de/fuhrpark/manager/FuhrparkManager.java
package de.fuhrpark.manager;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.base.FahrzeugService;
import java.util.List;

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
        if (fahrzeugService == null || fahrzeugFactory == null) {
            throw new IllegalArgumentException("Services dürfen nicht null sein");
        }
        this.fahrzeugService = fahrzeugService;
        this.fahrzeugFactory = fahrzeugFactory;
    }

    public void addFahrzeug(FahrzeugTyp typ, String kennzeichen, String marke, String modell, double preis) {
        Fahrzeug fahrzeug = fahrzeugFactory.createFahrzeug(typ, kennzeichen, marke, modell, preis);
        fahrzeugService.addFahrzeug(fahrzeug);
    }

    public List<Fahrzeug> getAlleFahrzeuge() {
        return fahrzeugService.getAlleFahrzeuge();
    }

    public void deleteFahrzeug(String kennzeichen) {
        fahrzeugService.deleteFahrzeug(kennzeichen);
    }

    public void updateFahrzeug(Fahrzeug fahrzeug) {
        fahrzeugService.updateFahrzeug(fahrzeug);
    }

    public Fahrzeug getFahrzeug(String kennzeichen) {
        return fahrzeugService.getFahrzeug(kennzeichen);
    }
}

