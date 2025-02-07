package de.fuhrpark.manager;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.model.enums.ReparaturTyp;
import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

public class FuhrparkManagerTest {
    private FuhrparkManager manager;
    private FahrzeugService fahrzeugService;
    private FahrtenbuchService fahrtenbuchService;
    private ReparaturService reparaturService;

    @BeforeEach
    void setUp() {
        fahrzeugService = mock(FahrzeugService.class);
        fahrtenbuchService = mock(FahrtenbuchService.class);
        reparaturService = mock(ReparaturService.class);
        manager = new FuhrparkManager(fahrzeugService, fahrtenbuchService, reparaturService);
    }

    @Test
    void testAddFahrzeug() {
        // Given
        Fahrzeug testFahrzeug = new Fahrzeug(
            "B-AB 123",
            FahrzeugTyp.PKW,
            "BMW",
            "320i"
        );

        // When
        manager.addFahrzeug(testFahrzeug);

        // Then
        verify(fahrzeugService).saveFahrzeug(testFahrzeug);
    }

    @Test
    void testAddReparatur() {
        // Given
        String kennzeichen = "B-AB 123";
        ReparaturBuchEintrag reparatur = new ReparaturBuchEintrag(
            "Ölwechsel",
            150.0,
            "Werkstatt XYZ",
            LocalDate.now()
        );

        // When
        manager.addReparatur(kennzeichen, reparatur);

        // Then
        verify(reparaturService).addReparatur(kennzeichen, reparatur);
    }

    @Test
    void testGetFahrzeug() {
        // Given
        String kennzeichen = "B-AB 123";
        Fahrzeug expectedFahrzeug = new Fahrzeug(
            kennzeichen,
            FahrzeugTyp.PKW,
            "BMW",
            "320i"
        );
        when(fahrzeugService.getFahrzeugByKennzeichen(kennzeichen)).thenReturn(expectedFahrzeug);

        // When
        Fahrzeug result = manager.getFahrzeug(kennzeichen);

        // Then
        assertNotNull(result);
        assertEquals(kennzeichen, result.getKennzeichen());
        verify(fahrzeugService).getFahrzeugByKennzeichen(kennzeichen);
    }

    @Test
    void testDeleteFahrzeug() {
        // Given
        String kennzeichen = "B-AB 123";

        // When
        manager.deleteFahrzeug(kennzeichen);

        // Then
        verify(fahrzeugService).deleteFahrzeug(kennzeichen);
    }

    @Test
    void testFahrzeugManagement() {
        Fahrzeug testFahrzeug = new Fahrzeug(
            "B-TK-2023",
            "Tesla",
            "Model 3",
            FahrzeugTyp.PKW,
            2023,
            45000.0
        );

        fahrzeugService.addFahrzeug(testFahrzeug);
        assertEquals(1, fahrzeugService.getAlleFahrzeuge().size());
        
        FahrtenbuchEintrag fahrtenbuchEintrag = new FahrtenbuchEintrag(
            LocalDate.now(),
            "Berlin",
            "München",
            500.0,
            testFahrzeug.getKennzeichen()
        );
        fahrtenbuchService.addEintrag(fahrtenbuchEintrag);

        ReparaturBuchEintrag reparaturEintrag = new ReparaturBuchEintrag(
            LocalDate.now(),
            ReparaturTyp.INSPEKTION,
            "Jahresinspektion",
            500.0,
            testFahrzeug.getKennzeichen(),
            "Werkstatt Berlin"
        );
        reparaturService.addReparatur(reparaturEintrag);

        var fahrten = fahrtenbuchService.getEintraegeForFahrzeug(testFahrzeug.getKennzeichen());
        assertEquals(1, fahrten.size());

        var reparaturen = reparaturService.getReparaturenForFahrzeug(testFahrzeug.getKennzeichen());
        assertEquals(1, reparaturen.size());
    }

    @Test
    void testAddFahrtenbuchEintrag() {
        FahrtenbuchEintrag eintrag = new FahrtenbuchEintrag(
            LocalDate.now(),
            "Berlin",
            "Hamburg",
            500.0,
            "B-AA 1234",
            "Max Mustermann"
        );
        fahrtenbuchService.addEintrag(eintrag);
        var eintraege = fahrtenbuchService.getEintraegeForFahrzeug("B-AA 1234");
        assertTrue(eintraege.contains(eintrag));
    }
} 