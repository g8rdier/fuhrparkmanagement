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

/**
 * Testet die Funktionalität des FuhrparkManagers
 */
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
        
        Fahrzeug gefunden = fahrzeugService.findeFahrzeugNachKennzeichen(kennzeichen);
        assertNotNull(gefunden, "Gefundenes Fahrzeug sollte nicht null sein");
        assertEquals(kennzeichen, gefunden.getKennzeichen(), "Kennzeichen stimmt nicht überein");
    }

    @Test
    public void testFahrzeugLoeschen() {
        String kennzeichen = "B-DEL 777";
        manager.createFahrzeug("PKW", kennzeichen, "Audi", "A4", 5, true);
        
        fahrzeugService.loescheFahrzeug(kennzeichen);
        assertNull(fahrzeugService.findeFahrzeugNachKennzeichen(kennzeichen), 
                  "Gelöschtes Fahrzeug sollte nicht mehr findbar sein");
    }

    @Test
    public void testCreateFahrzeug() {
        String kennzeichen = "B-TEST 123";
        String marke = "BMW";
        String modell = "X5";
        double preis = 50000.0;
        
        Fahrzeug mockPKW = new PKW(marke, modell, kennzeichen, preis);
        when(fahrzeugFactory.erstelleFahrzeug(FahrzeugTyp.PKW, marke, modell, kennzeichen, preis))
            .thenReturn(mockPKW);
        
        Fahrzeug result = manager.createFahrzeug("PKW", kennzeichen, marke, modell, preis);
        
        assertNotNull(result);
        assertEquals(kennzeichen, result.getKennzeichen());
        assertEquals(marke, result.getMarke());
        assertEquals(modell, result.getModell());
        
        verify(fahrzeugService).speichereFahrzeug(mockPKW);
    }

    @Test
    public void testDeleteFahrzeug() {
        String kennzeichen = "B-TEST 456";
        
        manager.deleteFahrzeug(kennzeichen);
        
        verify(fahrzeugService).loescheFahrzeug(kennzeichen);
    }
}