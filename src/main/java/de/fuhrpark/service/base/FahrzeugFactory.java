package de.fuhrpark.service.base;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.base.FahrzeugTyp;

/**
 * Interface für die Fahrzeug-Fabrik.
 * Ermöglicht die Erstellung verschiedener Fahrzeugtypen.
 */
public interface FahrzeugFactory {
    /**
     * Erstellt ein neues Fahrzeug basierend auf den übergebenen Parametern
     */
    Fahrzeug erstelleFahrzeug(FahrzeugTyp typ, String marke, String modell, String kennzeichen, double preis);
} 