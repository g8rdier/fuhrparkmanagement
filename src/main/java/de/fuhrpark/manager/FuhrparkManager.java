// src/main/java/de/fuhrpark/manager/FuhrparkManager.java
package de.fuhrpark.manager;

import de.fuhrpark.service.FahrzeugFactory;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.model.Fahrzeug;
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

    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeugService.speichereFahrzeug(fahrzeug);
    }

    public Fahrzeug getFahrzeug(String kennzeichen) {
        return fahrzeugService.findeFahrzeugNachKennzeichen(kennzeichen);
    }

    public List<Fahrzeug> getAlleFahrzeuge() {
        return fahrzeugService.findeAlleFahrzeuge();
    }

    public void deleteFahrzeug(String kennzeichen) {
        fahrzeugService.loescheFahrzeug(kennzeichen);
    }
}
