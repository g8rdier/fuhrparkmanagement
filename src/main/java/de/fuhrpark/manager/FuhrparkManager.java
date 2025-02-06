// src/main/java/de/fuhrpark/manager/FuhrparkManager.java
package de.fuhrpark.manager;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    // Fahrzeug management
    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeugService.addFahrzeug(fahrzeug);
    }

    public Fahrzeug getFahrzeugByKennzeichen(String kennzeichen) {
        return fahrzeugService.getFahrzeugByKennzeichen(kennzeichen);
    }

    public List<Fahrzeug> filterFahrzeuge(Predicate<Fahrzeug> filter) {
        return fahrzeugService.getAlleFahrzeuge().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    // Fahrtenbuch management
    public void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag) {
        Fahrzeug fahrzeug = getFahrzeugByKennzeichen(eintrag.getFahrzeugKennzeichen());
        if (fahrzeug != null && fahrzeug.isVerfuegbar()) {
            fahrtenbuchService.addEintrag(eintrag);
            fahrzeug.setStatus("in Benutzung");
            fahrzeugService.updateFahrzeug(fahrzeug);
        }
    }

    // Reparatur management
    public void addReparatur(ReparaturBuchEintrag reparatur) {
        Fahrzeug fahrzeug = getFahrzeugByKennzeichen(reparatur.getFahrzeugKennzeichen());
        if (fahrzeug != null) {
            reparaturService.addReparatur(reparatur);
            fahrzeug.setStatus("in Reparatur");
            fahrzeug.setAktuellerWert(fahrzeug.getAktuellerWert() + reparatur.getKosten() * 0.5);
            fahrzeugService.updateFahrzeug(fahrzeug);
        }
    }

    public List<FahrtenbuchEintrag> getFahrtenbuch(String kennzeichen) {
        return fahrtenbuchService.getEintraegeForFahrzeug(kennzeichen);
    }

    public List<ReparaturBuchEintrag> getReparaturen(String kennzeichen) {
        return reparaturService.getReparaturenForFahrzeug(kennzeichen);
    }
}
