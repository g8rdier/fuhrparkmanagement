package de.fuhrpark;

import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.model.Fahrzeug;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für den FuhrparkManager.
 */
class FuhrParkManagerTest {

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

        // Use method reference instead of lambda
        List<Fahrzeug> verfuegbare = manager.filterFahrzeuge(Fahrzeug::isVerfuegbar);
        assertTrue(verfuegbare.isEmpty());
    }
}
