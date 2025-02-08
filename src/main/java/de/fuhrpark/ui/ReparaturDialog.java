package de.fuhrpark.ui;

import de.fuhrpark.model.ReparaturBuchEintrag;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        okButton.addActionListener(e -> saveAndClose());
        add(okButton);

        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void saveAndClose() {
        try {
            LocalDate datum = LocalDate.parse(datumField.getText(), DATE_FORMATTER);
            double kosten = Double.parseDouble(kostenField.getText().replace(",", "."));
            
            result = new ReparaturBuchEintrag(
                datum,
                beschreibungField.getText(),
                kosten,
                werkstattField.getText()
            );
            
            dispose();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Ungültiges Datumsformat. Bitte TT.MM.JJJJ verwenden.",
                "Fehler",
                JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Ungültiges Kostenformat. Bitte eine Zahl eingeben.",
                "Fehler",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public ReparaturBuchEintrag getResult() {
        return result;
    }
} 