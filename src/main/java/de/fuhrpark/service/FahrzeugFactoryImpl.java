package de.fuhrpark.service.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.PKW;
import de.fuhrpark.model.LKW;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.model.enums.FahrzeugTyp;

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
        if (typ == null || kennzeichen == null || marke == null || modell == null) {
            throw new IllegalArgumentException("Keine der Hauptparameter darf null sein");
        }
        if (zusatzParameter == null || zusatzParameter.length < 2) {
            throw new IllegalArgumentException("Zusatzparameter müssen angegeben werden");
        }

        try {
            FahrzeugTyp fahrzeugTyp = FahrzeugTyp.valueOf(typ.toUpperCase());
            switch (fahrzeugTyp) {
                case PKW:
                    int sitzplaetze = (int) zusatzParameter[0];
                    boolean hatKlimaanlage = (boolean) zusatzParameter[1];
                    return new PKW(kennzeichen, marke, modell, sitzplaetze, hatKlimaanlage);
                    
                case LKW:
                    double ladekapazitaet = (double) zusatzParameter[0];
                    boolean hatAnhaengerkupplung = (boolean) zusatzParameter[1];
                    return new LKW(kennzeichen, marke, modell, ladekapazitaet, hatAnhaengerkupplung);
                    
                default:
                    throw new IllegalArgumentException("Unbekannter Fahrzeugtyp: " + typ);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ungültiger Fahrzeugtyp: " + typ, e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Ungültige Zusatzparameter für Typ " + typ, e);
        }
    }
}