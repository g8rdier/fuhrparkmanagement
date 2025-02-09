package de.fuhrpark.unit.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.impl.PKW;
import de.fuhrpark.model.impl.LKW;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.persistence.repository.DataStore;
import de.fuhrpark.manager.FuhrparkManager;

public class FuhrparkManagerTest {
    @Mock
    private FahrzeugService fahrzeugService;
    
    @Mock
    private FahrzeugFactory fahrzeugFactory;
    
    @Mock
    private DataStore dataStore;
    
    private FuhrparkManager manager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        manager = new FuhrparkManager(fahrzeugService, fahrzeugFactory, dataStore);
    }

    @Test
    public void testCreatePKW() {
        // Arrange
        String kennzeichen = "B-XX 1234";
        String marke = "BMW";
        String modell = "X5";
        double preis = 50000.0;
        PKW expectedPKW = new PKW(marke, modell, kennzeichen, preis);
        
        when(fahrzeugFactory.erstelleFahrzeug(FahrzeugTyp.PKW, marke, modell, kennzeichen, preis))
            .thenReturn(expectedPKW);

        // Act
        Fahrzeug pkw = manager.createFahrzeug("PKW", kennzeichen, marke, modell, preis);

        // Assert
        assertNotNull(pkw);
        assertTrue(pkw instanceof PKW);
        assertEquals(kennzeichen, pkw.getKennzeichen());
        verify(fahrzeugService).speichereFahrzeug(pkw);
    }

    @Test
    public void testCreateLKW() {
        // Arrange
        String kennzeichen = "B-YY 5678";
        String marke = "MAN";
        String modell = "TGX";
        double preis = 150000.0;
        LKW expectedLKW = new LKW(marke, modell, kennzeichen, preis);
        
        when(fahrzeugFactory.erstelleFahrzeug(FahrzeugTyp.LKW, marke, modell, kennzeichen, preis))
            .thenReturn(expectedLKW);

        // Act
        Fahrzeug lkw = manager.createFahrzeug("LKW", kennzeichen, marke, modell, preis);

        // Assert
        assertNotNull(lkw);
        assertTrue(lkw instanceof LKW);
        assertEquals(kennzeichen, lkw.getKennzeichen());
        verify(fahrzeugService).speichereFahrzeug(lkw);
    }

    @Test
    public void testInvalidFahrzeugType() {
        assertThrows(IllegalArgumentException.class, () -> 
            manager.createFahrzeug("INVALID", "B-ZZ 9999", "Test", "Test", 10000.0)
        );
    }

    @Test
    public void testDeleteFahrzeug() {
        // Arrange
        String kennzeichen = "B-TEST 456";
        
        // Act
        manager.deleteFahrzeug(kennzeichen);
        
        // Assert
        verify(fahrzeugService).loescheFahrzeug(kennzeichen);
    }
}