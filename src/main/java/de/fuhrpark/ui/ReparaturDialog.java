package de.fuhrpark.ui;

import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.service.ReparaturService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReparaturDialog extends JDialog {
    private JTextField beschreibungField;
    private JTextField kostenField;
    private JTextField werkstattField;
    private JTextField datumField;
    private ReparaturBuchEintrag result = null;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public ReparaturDialog(JFrame parent, String title) {
        super(parent, title, true);
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(5, 2, 5, 5));
        
        add(new JLabel("Beschreibung:"));
        beschreibungField = new JTextField();
        add(beschreibungField);

        add(new JLabel("Kosten (€):"));
        kostenField = new JTextField();
        add(kostenField);

        add(new JLabel("Werkstatt:"));
        werkstattField = new JTextField();
        add(werkstattField);

        add(new JLabel("Datum (TT.MM.JJJJ):"));
        datumField = new JTextField();
        datumField.setText(LocalDate.now().format(DATE_FORMATTER));
        add(datumField);

    
        JButton okButton = new JButton("Speichern");
        okButton.addActionListener(e -> handleAddReparatur());
        add(okButton);

        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void handleAddReparatur() {
        try {
            LocalDate datum = LocalDate.now();
            ReparaturBuchEintrag reparatur = new ReparaturBuchEintrag(
                datum,
                beschreibungField.getText(),
                Double.parseDouble(kostenField.getText()),
                werkstattField.getText()
            );
            
            result = reparatur;
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Bitte geben Sie einen gültigen Betrag für die Kosten ein.",
                "Eingabefehler",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public ReparaturBuchEintrag getResult() {
        return result;
    }
} 