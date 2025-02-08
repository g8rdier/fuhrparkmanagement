package de.fuhrpark.manager;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.PKW;
import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FuhrparkManagerTest {
    private FuhrparkManager manager;
    private DataStore dataStore;

    @BeforeEach
    void setUp() {
        dataStore = new DataStoreImpl();
        manager = new FuhrparkManager(dataStore);
    }

    @Test
    void testAddAndGetFahrzeug() {
        Fahrzeug pkw = new PKW("B-AA 123");
        manager.addFahrzeug(pkw);
        
        Fahrzeug retrieved = manager.getFahrzeug("B-AA 123");
        assertNotNull(retrieved);
        assertEquals("B-AA 123", retrieved.getKennzeichen());
        assertEquals("PKW", retrieved.getTyp());
    }

    @Test
    void testGetAlleFahrzeuge() {
        Fahrzeug pkw1 = new PKW("B-AA 123");
        Fahrzeug pkw2 = new PKW("B-BB 456");
        
        manager.addFahrzeug(pkw1);
        manager.addFahrzeug(pkw2);
        
        List<Fahrzeug> fahrzeuge = manager.getAlleFahrzeuge();
        assertEquals(2, fahrzeuge.size());
        assertTrue(fahrzeuge.stream().map(Fahrzeug::getKennzeichen).anyMatch(k -> k.equals("B-AA 123")));
        assertTrue(fahrzeuge.stream().map(Fahrzeug::getKennzeichen).anyMatch(k -> k.equals("B-BB 456")));
    }

    @Test
    void testDeleteFahrzeug() {
        Fahrzeug pkw = new PKW("B-AA 123");
        manager.addFahrzeug(pkw);
        
        manager.deleteFahrzeug("B-AA 123");
        assertNull(manager.getFahrzeug("B-AA 123"));
    }
}