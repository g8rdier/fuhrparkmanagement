package de.fuhrpark.manager.test;

import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;
import de.fuhrpark.service.FahrzeugFactory;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.PKW;
import de.fuhrpark.model.LKW;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testet die Funktionalität des FuhrparkManagers
 */
public class FuhrparkManagerTest {
    private FuhrparkManager manager;
    private DataStore dataStore;
    private FahrzeugFactory factory;
    private FahrzeugService service;

    @Before
    public void setUp() {
        // Initialisierung der Komponenten
        dataStore = new DataStoreImpl();
        factory = new FahrzeugFactoryImpl();
        service = new FahrzeugServiceImpl(dataStore);
        manager = new FuhrparkManager(service, factory);
    }

    @Test
    public void testErstellePKW() {
        // Test PKW erstellen
        Fahrzeug pkw = manager.erstelleNeuesFahrzeug(
            "PKW", 
            "B-TK 1234", 
            "BMW", 
            "X5", 
            5,  // Sitzplätze
            true // Klimaanlage
        );
        
        assertNotNull("Fahrzeug sollte erstellt worden sein", pkw);
        assertTrue("Fahrzeug sollte ein PKW sein", pkw instanceof PKW);
        assertEquals("Kennzeichen sollte übereinstimmen", "B-TK 1234", pkw.getKennzeichen());
    }

    @Test
    public void testErstelleLKW() {
        // Test LKW erstellen
        Fahrzeug lkw = manager.erstelleNeuesFahrzeug(
            "LKW", 
            "B-LK 5678", 
            "MAN", 
            "TGX", 
            7.5,  // Ladekapazität
            true  // Anhängerkupplung
        );
        
        assertNotNull("Fahrzeug sollte erstellt worden sein", lkw);
        assertTrue("Fahrzeug sollte ein LKW sein", lkw instanceof LKW);
        assertEquals("Kennzeichen sollte übereinstimmen", "B-LK 5678", lkw.getKennzeichen());
    }

    @Test
    public void testFahrzeugFinden() {
        // Fahrzeug erstellen und dann finden
        String kennzeichen = "B-TEST 999";
        manager.erstelleNeuesFahrzeug("PKW", kennzeichen, "VW", "Golf", 5, true);
        
        Fahrzeug gefunden = service.findeFahrzeugNachKennzeichen(kennzeichen);
        assertNotNull("Fahrzeug sollte gefunden werden", gefunden);
        assertEquals("Kennzeichen sollte übereinstimmen", kennzeichen, gefunden.getKennzeichen());
    }

    @Test
    public void testFahrzeugLoeschen() {
        // Fahrzeug erstellen und dann löschen
        String kennzeichen = "B-DEL 777";
        manager.erstelleNeuesFahrzeug("PKW", kennzeichen, "Audi", "A4", 5, true);
        
        service.loescheFahrzeug(kennzeichen);
        assertNull("Fahrzeug sollte gelöscht sein", 
                  service.findeFahrzeugNachKennzeichen(kennzeichen));
    }
}