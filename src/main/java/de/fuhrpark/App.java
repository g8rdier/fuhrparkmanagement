package de.fuhrpark;

import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DatabaseDataStoreImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrtenbuchServiceImpl;
import de.fuhrpark.service.impl.ReparaturServiceImpl;
import de.fuhrpark.ui.FuhrparkUI;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        DataStore dataStore = new DatabaseDataStoreImpl();
        
        var fahrzeugService = new FahrzeugServiceImpl(dataStore);
        var fahrtenbuchService = new FahrtenbuchServiceImpl(dataStore);
        var reparaturService = new ReparaturServiceImpl(dataStore);
        
        var ui = new FuhrparkUI(fahrzeugService, fahrtenbuchService, reparaturService);
        
        ui.setVisible(true);
    }
}
