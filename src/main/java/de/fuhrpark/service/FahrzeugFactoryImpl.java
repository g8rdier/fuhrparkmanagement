package de.fuhrpark.service.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.PKW;
import de.fuhrpark.model.LKW;
import de.fuhrpark.service.FahrzeugFactory;

/**
 * Singleton-Implementierung der FahrzeugFactory
 */
public class FahrzeugFactoryImpl implements FahrzeugFactory {
    private static FahrzeugFactoryImpl instance;
    
    private FahrzeugFactoryImpl() {}
    
    public static FahrzeugFactoryImpl getInstance() {
        if (instance == null) {
            instance = new FahrzeugFactoryImpl();
        }
        return instance;
    }

    @Override
    public Fahrzeug erstelleFahrzeug(String typ, String kennzeichen, String marke, 
                                    String modell, Object... zusatzParameter) {
        switch (typ.toUpperCase()) {
            case "PKW":
                int sitzplaetze = (int) zusatzParameter[0];
                boolean hatKlimaanlage = (boolean) zusatzParameter[1];
                return new PKW(kennzeichen, marke, modell, sitzplaetze, hatKlimaanlage);
                
            case "LKW":
                double ladekapazitaet = (double) zusatzParameter[0];
                boolean hatAnhaengerkupplung = (boolean) zusatzParameter[1];
                return new LKW(kennzeichen, marke, modell, ladekapazitaet, hatAnhaengerkupplung);
                
            default:
                throw new IllegalArgumentException("Unbekannter Fahrzeugtyp: " + typ);
        }
    }
} 