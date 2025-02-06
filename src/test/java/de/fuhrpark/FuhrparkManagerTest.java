package de.fuhrpark;

import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrtenbuchServiceImpl;
import de.fuhrpark.service.impl.ReparaturServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für den FuhrparkManager.
 */
public class FuhrparkManagerTest {
    private FuhrparkManager manager;
    private FahrzeugService fahrzeugService;
    private FahrtenbuchService fahrtenbuchService;
    private ReparaturService reparaturService;

    @BeforeEach
    public void setup() {
        fahrzeugService = new FahrzeugServiceImpl();
        fahrtenbuchService = new FahrtenbuchServiceImpl();
        reparaturService = new ReparaturServiceImpl();
        manager = new FuhrparkManager(fahrzeugService, fahrtenbuchService, reparaturService);
    }

    @Test
    public void testAddAndRetrieveFahrzeug() {
        Fahrzeug testFahrzeug = new Fahrzeug("B-TK 1234", "BMW", "X5", 
                                            FahrzeugTyp.PKW, 2020, 50000.0);
        manager.addFahrzeug(testFahrzeug);
        
        Fahrzeug retrieved = manager.getFahrzeugByKennzeichen("B-TK 1234");
        assertNotNull(retrieved, "Retrieved vehicle should not be null");
        assertEquals("B-TK 1234", retrieved.getKennzeichen(), "Kennzeichen should match");
        assertEquals("BMW", retrieved.getMarke(), "Marke should match");
    }

    @Test
    public void testFilterFahrzeuge() {
        Fahrzeug pkw = new Fahrzeug("B-TK 1234", "BMW", "X5", 
                                   FahrzeugTyp.PKW, 2020, 50000.0);
        Fahrzeug lkw = new Fahrzeug("B-TK 5678", "MAN", "TGX", 
                                   FahrzeugTyp.LKW, 2019, 80000.0);
        
        manager.addFahrzeug(pkw);
        manager.addFahrzeug(lkw);
        
        assertEquals(1, manager.filterFahrzeuge(f -> f.getTyp() == FahrzeugTyp.PKW).size(),
                    "Should find one PKW");
        assertEquals(1, manager.filterFahrzeuge(f -> f.getTyp() == FahrzeugTyp.LKW).size(),
                    "Should find one LKW");
    }
}
