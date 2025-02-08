package de.fuhrpark.manager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;
import de.fuhrpark.service.FahrzeugFactory;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.PKW;
import de.fuhrpark.model.LKW;

/**
 * Testet die Funktionalität des FuhrparkManagers
 */
public class FuhrparkManagerTest {
    private FuhrparkManager manager;
    private DataStore dataStore;
    private FahrzeugService service;

    @BeforeEach
    public void setUp() {
        dataStore = new DataStoreImpl();
        service = new FahrzeugServiceImpl(dataStore);
        manager = new FuhrparkManager(service, FahrzeugFactoryImpl.getInstance());
    }

    @Test
    public void testErstellePKW() {
        Fahrzeug pkw = manager.erstelleNeuesFahrzeug(
            "PKW", 
            "B-TK 1234", 
            "BMW", 
            "X5", 
            5, 
            true
        );
        
        assertNotNull(pkw);
        assertTrue(pkw instanceof PKW);
        assertEquals("B-TK 1234", pkw.getKennzeichen());
    }

    @Test
    public void testErstelleLKW() {
        Fahrzeug lkw = manager.erstelleNeuesFahrzeug(
            "LKW", 
            "B-LK 5678", 
            "MAN", 
            "TGX", 
            7.5, 
            true
        );
        
        assertNotNull(lkw);
        assertTrue(lkw instanceof LKW);
        assertEquals("B-LK 5678", lkw.getKennzeichen());
    }

    @Test
    public void testFahrzeugFinden() {
        String kennzeichen = "B-TEST 999";
        manager.erstelleNeuesFahrzeug("PKW", kennzeichen, "VW", "Golf", 5, true);
        
        Fahrzeug gefunden = service.findeFahrzeugNachKennzeichen(kennzeichen);
        assertNotNull(gefunden);
        assertEquals(kennzeichen, gefunden.getKennzeichen());
    }

    @Test
    public void testFahrzeugLoeschen() {
        String kennzeichen = "B-DEL 777";
        manager.erstelleNeuesFahrzeug("PKW", kennzeichen, "Audi", "A4", 5, true);
        
        service.loescheFahrzeug(kennzeichen);
        assertNull(service.findeFahrzeugNachKennzeichen(kennzeichen));
    }
}