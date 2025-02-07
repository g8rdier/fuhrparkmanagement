package de.fuhrpark.manager;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
            "BMW",
            "320i",
            FahrzeugTyp.PKW,
            2020,
            50000.0
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
            "BMW",
            "320i",
            FahrzeugTyp.PKW,
            2020,
            50000.0
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
}