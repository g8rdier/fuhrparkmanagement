package de.fuhrpark.service;

import de.fuhrpark.model.Fahrzeug;
import java.util.List;

public interface FahrzeugService {
    void saveFahrzeug(Fahrzeug fahrzeug);
    Fahrzeug getFahrzeug(String kennzeichen);
    List<Fahrzeug> getAlleFahrzeuge();
    void deleteFahrzeug(String kennzeichen);
    void addFahrzeug(Fahrzeug fahrzeug);
    void updateFahrzeug(Fahrzeug fahrzeug);
    Fahrzeug getFahrzeugByKennzeichen(String kennzeichen);
}