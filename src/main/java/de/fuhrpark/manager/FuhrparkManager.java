// src/main/java/de/fuhrpark/manager/FuhrparkManager.java
package de.fuhrpark.manager;

import de.fuhrpark.model.base.Fahrzeug;
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
    public Fahrzeug erstelleNeuesFahrzeug(String typ, String kennzeichen, String marke, 
                                         String modell, Object... zusatzParameter) {
        Fahrzeug fahrzeug = fahrzeugFactory.erstelleFahrzeug(typ, kennzeichen, marke, 
                                                            modell, zusatzParameter);
        fahrzeugService.speichereFahrzeug(fahrzeug);
        return fahrzeug;
    }

    public void aktualisiereFahrzeug(Fahrzeug fahrzeug) {
        fahrzeugService.aktualisiereFahrzeug(fahrzeug);
    }

    public void loescheFahrzeug(String kennzeichen) {
        fahrzeugService.loescheFahrzeug(kennzeichen);
    }

    public Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen) {
        return fahrzeugService.findeFahrzeugNachKennzeichen(kennzeichen);
    }
}
