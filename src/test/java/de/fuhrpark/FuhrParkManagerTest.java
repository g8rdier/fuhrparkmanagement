// src/test/java/de/fuhrpark/FuhrparkManagerTest.java
package de.fuhrpark;

import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.model.Fahrzeug;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuhrparkManagerTest {
    @Test
    void testAddFahrzeug() {
        FuhrparkManager manager = new FuhrparkManager();
        manager.addFahrzeug(new Fahrzeug("K-ABC123", "VW Golf", 2020));
        assertEquals(1, manager.filterFahrzeuge(f -> true).size());
    }

    @Test
    void testFilterVerfuegbar() {
        FuhrparkManager manager = new FuhrparkManager();
        Fahrzeug f1 = new Fahrzeug("K-DEF456", "Mercedes C-Class", 2021);
        f1.setStatus("in Wartung");
        manager.addFahrzeug(f1);
        
        List<Fahrzeug> verfuegbare = manager.filterFahrzeuge(f -> f.getStatus().equals("verfügbar"));
        assertTrue(verfuegbare.isEmpty());
    }
}
