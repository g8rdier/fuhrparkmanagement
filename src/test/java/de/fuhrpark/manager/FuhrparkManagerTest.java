package de.fuhrpark.manager;

import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrtenbuchServiceImpl;
import de.fuhrpark.service.impl.ReparaturServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testBasicFunctionality() {
        // Test implementation will come later
    }
} 