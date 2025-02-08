package de.fuhrpark.unit.manager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fuhrpark.persistence.repository.DataStore;
import de.fuhrpark.persistence.impl.DatabaseDataStoreImpl;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.impl.PKW;
import de.fuhrpark.model.impl.LKW;
import de.fuhrpark.manager.FuhrparkManager;

/**
 * Testet die Funktionalität des FuhrparkManagers
 */
public class FuhrparkManagerTest {
    private DataStore dataStore;
    private FahrzeugService service;
    private FahrzeugFactory factory;
    private FuhrparkManager manager;

    @BeforeEach
    public void setUp() {
        dataStore = new DatabaseDataStoreImpl();
        service = new FahrzeugServiceImpl(dataStore);
        factory = new FahrzeugFactoryImpl();
        manager = new FuhrparkManager(service, factory);
    }

    @Test
    public void testCreatePKW() {
        Fahrzeug pkw = manager.createFahrzeug("PKW", "B-XX 1234", "BMW", "X5", 50000.0);
        assertNotNull(pkw);
        assertTrue(pkw instanceof PKW);
        assertEquals("B-XX 1234", pkw.getKennzeichen());
    }

    @Test
    public void testCreateLKW() {
        Fahrzeug lkw = manager.createFahrzeug("LKW", "B-YY 5678", "MAN", "TGX", 150000.0);
        assertNotNull(lkw);
        assertTrue(lkw instanceof LKW);
        assertEquals("B-YY 5678", lkw.getKennzeichen());
    }

    @Test
    public void testInvalidFahrzeugType() {
        assertThrows(IllegalArgumentException.class, () -> 
            manager.createFahrzeug("INVALID", "B-ZZ 9999", "Test", "Test", 10000.0)
        );
    }

    @Test
    public void testFahrzeugFinden() {
        String kennzeichen = "B-TEST 999";
        manager.createFahrzeug("PKW", kennzeichen, "VW", "Golf", 5, true);
        
        Fahrzeug gefunden = service.findeFahrzeugNachKennzeichen(kennzeichen);
        assertNotNull(gefunden, "Gefundenes Fahrzeug sollte nicht null sein");
        assertEquals(kennzeichen, gefunden.getKennzeichen(), "Kennzeichen stimmt nicht überein");
    }

    @Test
    public void testFahrzeugLoeschen() {
        String kennzeichen = "B-DEL 777";
        manager.createFahrzeug("PKW", kennzeichen, "Audi", "A4", 5, true);
        
        service.loescheFahrzeug(kennzeichen);
        assertNull(service.findeFahrzeugNachKennzeichen(kennzeichen), 
                  "Gelöschtes Fahrzeug sollte nicht mehr findbar sein");
    }
}