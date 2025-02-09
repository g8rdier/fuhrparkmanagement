package de.fuhrpark.unit.manager;

import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.impl.PKW;
import de.fuhrpark.model.impl.LKW;
import de.fuhrpark.service.base.FahrzeugFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FuhrparkManagerTest {
    private FuhrparkManager manager;
    private FahrzeugFactory factory;

    @BeforeEach
    void setUp() {
        factory = mock(FahrzeugFactory.class);
        manager = new FuhrparkManager();
    }

    @Test
    void shouldAddPKWToFuhrpark() {
        // Given
        PKW pkw = new PKW("B-XX 1234", "BMW", "X3", 50000.0);
        when(factory.createFahrzeug("PKW", "B-XX 1234", "BMW", "X3", 50000.0))
            .thenReturn(pkw);

        // When
        manager.addFahrzeug(pkw);

        // Then
        assertTrue(manager.getAlleFahrzeuge().contains(pkw));
        assertEquals(1, manager.getAlleFahrzeuge().size());
    }

    @Test
    void shouldAddLKWToFuhrpark() {
        // Given
        LKW lkw = new LKW("B-YY 5678", "MAN", "TGX", 150000.0);
        when(factory.createFahrzeug("LKW", "B-YY 5678", "MAN", "TGX", 150000.0))
            .thenReturn(lkw);

        // When
        manager.addFahrzeug(lkw);

        // Then
        assertTrue(manager.getAlleFahrzeuge().contains(lkw));
        assertEquals(1, manager.getAlleFahrzeuge().size());
    }

    @Test
    void shouldCreateAndAddPKW() {
        // Given
        PKW pkw = new PKW("B-XX 1234", "BMW", "X3", 50000.0);
        when(factory.createFahrzeug("PKW", "B-XX 1234", "BMW", "X3", 50000.0))
            .thenReturn(pkw);

        // When
        Fahrzeug created = manager.createFahrzeug("PKW", "B-XX 1234", "BMW", "X3", 50000.0);
        manager.addFahrzeug(created);

        // Then
        assertEquals(1, manager.getAlleFahrzeuge().size());
        assertEquals("PKW", created.getTyp());
    }

    @Test
    void shouldCreateAndAddLKW() {
        // Given
        LKW lkw = new LKW("B-YY 5678", "MAN", "TGX", 150000.0);
        when(factory.createFahrzeug("LKW", "B-YY 5678", "MAN", "TGX", 150000.0))
            .thenReturn(lkw);

        // When
        Fahrzeug created = manager.createFahrzeug("LKW", "B-YY 5678", "MAN", "TGX", 150000.0);
        manager.addFahrzeug(created);

        // Then
        assertEquals(1, manager.getAlleFahrzeuge().size());
        assertEquals("LKW", created.getTyp());
    }
}