package de.fuhrpark.unit.manager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fuhrpark.persistence.repository.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.PKW;
import de.fuhrpark.model.LKW;
import de.fuhrpark.manager.FuhrparkManager;

/**
 * Testet die Funktionalität des FuhrparkManagers
 */
public class FuhrparkManagerTest {
    private FuhrparkManager manager;
    private DataStore dataStore;
    private FahrzeugService service;
    private FahrzeugFactory factory;

    @BeforeEach
    public void setUp() {
        dataStore = new DataStoreImpl();
        service = new FahrzeugServiceImpl(dataStore);
        factory = new FahrzeugFactoryImpl();
        manager = new FuhrparkManager(service, factory);
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
        
        assertNotNull(pkw, "Erstelltes PKW sollte nicht null sein");
        assertTrue(pkw instanceof PKW, "Erstelltes Fahrzeug sollte ein PKW sein");
        assertEquals("B-TK 1234", pkw.getKennzeichen(), "Kennzeichen stimmt nicht überein");
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
        
        assertNotNull(lkw, "Erstelltes LKW sollte nicht null sein");
        assertTrue(lkw instanceof LKW, "Erstelltes Fahrzeug sollte ein LKW sein");
        assertEquals("B-LK 5678", lkw.getKennzeichen(), "Kennzeichen stimmt nicht überein");
    }

    @Test
    public void testFahrzeugFinden() {
        String kennzeichen = "B-TEST 999";
        manager.erstelleNeuesFahrzeug("PKW", kennzeichen, "VW", "Golf", 5, true);
        
        Fahrzeug gefunden = service.findeFahrzeugNachKennzeichen(kennzeichen);
        assertNotNull(gefunden, "Gefundenes Fahrzeug sollte nicht null sein");
        assertEquals(kennzeichen, gefunden.getKennzeichen(), "Kennzeichen stimmt nicht überein");
    }

    @Test
    public void testFahrzeugLoeschen() {
        String kennzeichen = "B-DEL 777";
        manager.erstelleNeuesFahrzeug("PKW", kennzeichen, "Audi", "A4", 5, true);
        
        service.loescheFahrzeug(kennzeichen);
        assertNull(service.findeFahrzeugNachKennzeichen(kennzeichen), 
                  "Gelöschtes Fahrzeug sollte nicht mehr findbar sein");
    }
}