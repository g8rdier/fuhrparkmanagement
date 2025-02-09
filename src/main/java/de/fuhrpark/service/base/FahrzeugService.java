package de.fuhrpark.service.base;

import de.fuhrpark.model.base.Fahrzeug;
import java.util.List;

/**
 * Interface für die Fahrzeug-Verwaltung.
 * Definiert grundlegende Operationen für Fahrzeuge.
 */
public interface FahrzeugService {
    void addFahrzeug(Fahrzeug fahrzeug);
    void updateFahrzeug(Fahrzeug fahrzeug);
    Fahrzeug getFahrzeug(String kennzeichen);
    List<Fahrzeug> getAlleFahrzeuge();
    void deleteFahrzeug(String kennzeichen);
}