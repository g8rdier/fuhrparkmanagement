package de.fuhrpark.service;

import de.fuhrpark.model.Fahrzeug;

/**
 * Interface für die Fahrzeug-Fabrik.
 * Ermöglicht die Erstellung verschiedener Fahrzeugtypen.
 */
public interface FahrzeugFactory {
    /**
     * Erstellt ein neues Fahrzeug basierend auf den übergebenen Parametern
     */
    Fahrzeug erstelleFahrzeug(String typ, String kennzeichen, String marke, String modell, 
                             Object... zusatzParameter);
} 