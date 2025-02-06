package de.fuhrpark;

import de.fuhrpark.ui.FuhrparkUI;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrtenbuchServiceImpl;
import de.fuhrpark.service.impl.ReparaturServiceImpl;
import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(() -> {
            FahrzeugService fahrzeugService = new FahrzeugServiceImpl();
            FahrtenbuchService fahrtenbuchService = new FahrtenbuchServiceImpl();
            ReparaturService reparaturService = new ReparaturServiceImpl();
            
            FuhrparkUI ui = new FuhrparkUI(fahrzeugService, fahrtenbuchService, reparaturService);
            ui.setVisible(true);
        });
    }
}
