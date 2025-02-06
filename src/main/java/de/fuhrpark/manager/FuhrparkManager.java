// src/main/java/de/fuhrpark/manager/FuhrparkManager.java
package de.fuhrpark.manager;

import de.fuhrpark.model.*;
import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.service.*;
import de.fuhrpark.service.impl.*;
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

    public FuhrparkManager(DataStore dataStore) {
        this.fahrzeugService = new FahrzeugServiceImpl(dataStore);
        this.fahrtenbuchService = new FahrtenbuchServiceImpl(dataStore);
        this.reparaturService = new ReparaturServiceImpl(dataStore);
    }

    // Fahrzeug management
    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeugService.addFahrzeug(fahrzeug);
    }

    public void deleteFahrzeug(String kennzeichen) {
        fahrzeugService.deleteFahrzeug(kennzeichen);
    }

    public List<Fahrzeug> getAlleFahrzeuge() {
        return fahrzeugService.getAlleFahrzeuge();
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
    public void addEintrag(FahrtenbuchEintrag eintrag) {
        fahrtenbuchService.addEintrag(eintrag);
    }

    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen) {
        return fahrtenbuchService.getEintraegeForFahrzeug(kennzeichen);
    }

    // Reparatur management
    public void addReparatur(ReparaturBuchEintrag reparatur) {
        reparaturService.addReparatur(reparatur);
    }

    public List<ReparaturBuchEintrag> getReparaturen(String kennzeichen) {
        return reparaturService.getReparaturenForFahrzeug(kennzeichen);
    }
}
