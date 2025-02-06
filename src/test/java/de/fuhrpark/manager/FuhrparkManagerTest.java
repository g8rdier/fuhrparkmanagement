package de.fuhrpark.manager;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.model.enums.ReparaturTyp;
import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrtenbuchServiceImpl;
import de.fuhrpark.service.impl.ReparaturServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FuhrparkManagerTest {
    private DataStore dataStore;
    private FahrzeugServiceImpl fahrzeugService;
    private FahrtenbuchServiceImpl fahrtenbuchService;
    private ReparaturServiceImpl reparaturService;

    @BeforeEach
    void setUp() {
        dataStore = new DataStoreImpl();
        fahrzeugService = new FahrzeugServiceImpl(dataStore);
        fahrtenbuchService = new FahrtenbuchServiceImpl(dataStore);
        reparaturService = new ReparaturServiceImpl(dataStore);
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