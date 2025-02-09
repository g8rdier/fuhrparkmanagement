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
import de.fuhrpark.manager.FuhrparkManager;

public class FuhrparkManagerTest {
    @Mock
    private FahrzeugService fahrzeugService;
    
    @Mock
    private FahrzeugFactory fahrzeugFactory;
    
    private FuhrparkManager manager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        manager = new FuhrparkManager(fahrzeugService, fahrzeugFactory);
    }

    @Test
    public void testAddPKW() {
        // Arrange
        String kennzeichen = "B-XX 1234";
        String marke = "BMW";
        String modell = "X5";
        double preis = 50000.0;
        PKW expectedPKW = new PKW(kennzeichen, marke, modell, preis);
        
        when(fahrzeugFactory.createFahrzeug(FahrzeugTyp.PKW, kennzeichen, marke, modell, preis))
            .thenReturn(expectedPKW);

        // Act
        manager.addFahrzeug(FahrzeugTyp.PKW, kennzeichen, marke, modell, preis);

        // Assert
        verify(fahrzeugService).addFahrzeug(expectedPKW);
    }

    @Test
    public void testAddLKW() {
        // Arrange
        String kennzeichen = "B-YY 5678";
        String marke = "MAN";
        String modell = "TGX";
        double preis = 150000.0;
        LKW expectedLKW = new LKW(kennzeichen, marke, modell, preis);
        
        when(fahrzeugFactory.createFahrzeug(FahrzeugTyp.LKW, kennzeichen, marke, modell, preis))
            .thenReturn(expectedLKW);

        // Act
        manager.addFahrzeug(FahrzeugTyp.LKW, kennzeichen, marke, modell, preis);

        // Assert
        verify(fahrzeugService).addFahrzeug(expectedLKW);
    }

    @Test
    public void testDeleteFahrzeug() {
        // Arrange
        String kennzeichen = "B-TEST 456";
        
        // Act
        manager.deleteFahrzeug(kennzeichen);
        
        // Assert
        verify(fahrzeugService).deleteFahrzeug(kennzeichen);
    }

    @Test
    public void testGetFahrzeug() {
        // Arrange
        String kennzeichen = "B-TEST 123";
        PKW expectedPKW = new PKW(kennzeichen, "Test", "Test", 10000.0);
        when(fahrzeugService.getFahrzeug(kennzeichen)).thenReturn(expectedPKW);

        // Act
        Fahrzeug result = manager.getFahrzeug(kennzeichen);

        // Assert
        assertNotNull(result);
        assertEquals(kennzeichen, result.getKennzeichen());
        verify(fahrzeugService).getFahrzeug(kennzeichen);
    }
}