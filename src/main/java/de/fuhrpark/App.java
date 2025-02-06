package de.fuhrpark;

import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;
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
        DataStore dataStore = new DataStoreImpl();
        
        FuhrparkUI ui = new FuhrparkUI(
            new FahrzeugServiceImpl(dataStore),
            new FahrtenbuchServiceImpl(dataStore),
            new ReparaturServiceImpl(dataStore)
        );
        
        ui.setVisible(true);
    }
}
