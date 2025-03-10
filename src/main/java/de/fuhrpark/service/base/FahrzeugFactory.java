package de.fuhrpark.service.base;

import de.fuhrpark.model.base.Fahrzeug;

public interface FahrzeugFactory {
    /**
     * Creates a new vehicle instance based on the given type and parameters.
     *
     * @param typ The type of vehicle (PKW or LKW)
     * @param kennzeichen The license plate number
     * @param marke The brand of the vehicle
     * @param modell The model of the vehicle
     * @param preis The price of the vehicle
     * @return A new vehicle instance
     */
    Fahrzeug createFahrzeug(String typ, String kennzeichen, String marke, String modell, double preis);
} 