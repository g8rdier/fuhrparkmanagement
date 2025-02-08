package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.service.base.FahrtenbuchService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FahrtenbuchDialog extends JDialog {
    private final FahrtenbuchService fahrtenbuchService;
    private final String kennzeichen;
    private final DefaultListModel<FahrtenbuchEintrag> listModel;

    public FahrtenbuchDialog(Frame owner, FahrtenbuchService service, String kennzeichen) {
        super(owner, "Fahrtenbuch für " + kennzeichen, true);
        if (service == null || kennzeichen == null) {
            throw new IllegalArgumentException("Service und Kennzeichen dürfen nicht null sein");
        }
        this.fahrtenbuchService = service;
        this.kennzeichen = kennzeichen;
        this.listModel = new DefaultListModel<>();
        
        initComponents();
        loadFahrten();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        
        // List of entries
        JList<FahrtenbuchEintrag> list = new JList<>(listModel);
        add(new JScrollPane(list), BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Neue Fahrt");
        JButton closeButton = new JButton("Schließen");
        
        addButton.addActionListener(e -> addNewFahrt());
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(addButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setSize(400, 300);
        setLocationRelativeTo(getOwner());
    }

    private void loadFahrten() {
        listModel.clear();
        List<FahrtenbuchEintrag> fahrten = fahrtenbuchService.getFahrtenForFahrzeug(kennzeichen);
        fahrten.forEach(listModel::addElement);
    }

    private void addNewFahrt() {
        // Implementation for adding new entries will be added later
        JOptionPane.showMessageDialog(this, 
            "Neue Fahrt Funktion wird in der nächsten Version implementiert",
            "Info",
            JOptionPane.INFORMATION_MESSAGE);
    }
}