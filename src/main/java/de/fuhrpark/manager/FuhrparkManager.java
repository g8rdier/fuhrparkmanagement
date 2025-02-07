// src/main/java/de/fuhrpark/manager/FuhrparkManager.java
package de.fuhrpark.manager;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;

/**
 * Verwaltet die Fahrzeuge des Fuhrparks.
 */
public class FuhrparkManager {
    private final FahrzeugService fahrzeugService;
    private final FahrtenbuchService fahrtenbuchService;
    private final ReparaturService reparaturService;

    public FuhrparkManager(FahrzeugService fahrzeugService, 
                          FahrtenbuchService fahrtenbuchService,
                          ReparaturService reparaturService) {
        this.fahrzeugService = fahrzeugService;
        this.fahrtenbuchService = fahrtenbuchService;
        this.reparaturService = reparaturService;
    }

    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeugService.saveFahrzeug(fahrzeug);
    }

    public void addReparatur(String kennzeichen, ReparaturBuchEintrag reparatur) {
        reparaturService.addReparatur(kennzeichen, reparatur);
    }

    public Fahrzeug getFahrzeug(String kennzeichen) {
        return fahrzeugService.getFahrzeugByKennzeichen(kennzeichen);
    }

    public void deleteFahrzeug(String kennzeichen) {
        fahrzeugService.deleteFahrzeug(kennzeichen);
    }
}
