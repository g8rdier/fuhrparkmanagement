package de.fuhrpark.service.base;

import de.fuhrpark.model.base.Fahrzeug;

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