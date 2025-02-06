package de.fuhrpark.ui;

import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.model.enums.ReparaturTyp;
import de.fuhrpark.service.ReparaturService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class ReparaturDialog extends Dialog<ReparaturBuchEintrag> {
    private final JTable table;
    private final String kennzeichen;
    private final ReparaturService service;
    private final TextField kennzeichenField = new TextField();
    private final TextField werkstattField = new TextField();
    private final TextField kostenField = new TextField();
    private final TextField beschreibungField = new TextField();
    private final ComboBox<ReparaturTyp> typComboBox = new ComboBox<>();

    public ReparaturDialog(Frame owner, String kennzeichen, ReparaturService service) {
        super(owner, "Reparaturen: " + kennzeichen, true);
        this.kennzeichen = kennzeichen;
        this.service = service;

        // Create toolbar with New button
        JToolBar toolBar = new JToolBar();
        JButton newButton = new JButton("Neu");
        newButton.addActionListener(__ -> showNewEntryDialog());
        toolBar.add(newButton);

        // Table setup
        table = new JTable(new ReparaturTableModel(service.getReparaturenForFahrzeug(kennzeichen)));
        
        // Layout
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        setSize(600, 400);
        setLocationRelativeTo(owner);

        // Fix unused lambda parameters by using underscore
        getDialogPane().lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, _ -> {
            // ... validation logic ...
        });

        setResultConverter(_ -> {  // Use underscore for unused parameter
            // ... result conversion logic ...
            return new ReparaturBuchEintrag(/* parameters */);
        });
    }

    private void showNewEntryDialog() {
        JDialog dialog = new JDialog(this, "Neue Reparatur", true);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JComboBox<ReparaturTyp> typCombo = new JComboBox<>(ReparaturTyp.values());
        JTextField beschreibungField = new JTextField();
        JTextField kostenField = new JTextField();
        JTextField werkstattField = new JTextField();

        panel.add(new JLabel("Typ:"));
        panel.add(typCombo);
        panel.add(new JLabel("Beschreibung:"));
        panel.add(beschreibungField);
        panel.add(new JLabel("Kosten:"));
        panel.add(kostenField);
        panel.add(new JLabel("Werkstatt:"));
        panel.add(werkstattField);

        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(__ -> {
            try {
                double kosten = Double.parseDouble(kostenField.getText());
                service.addReparatur(new ReparaturBuchEintrag(
                    LocalDate.now(),
                    (ReparaturTyp)typCombo.getSelectedItem(),
                    beschreibungField.getText(),
                    kosten,
                    kennzeichen,
                    werkstattField.getText()
                ));
                ((ReparaturTableModel)table.getModel()).updateData(
                    service.getReparaturenForFahrzeug(kennzeichen)
                );
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Bitte geben Sie gültige Kosten ein.",
                    "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(saveButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public ReparaturDialog() {
        setTitle("Neue Reparatur");
        setHeaderText("Bitte geben Sie die Reparatur-Details ein");

        // Create the grid pane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Add fields to grid
        grid.add(new Label("Kennzeichen:"), 0, 0);
        grid.add(kennzeichenField, 1, 0);
        grid.add(new Label("Werkstatt:"), 0, 1);
        grid.add(werkstattField, 1, 1);
        grid.add(new Label("Kosten:"), 0, 2);
        grid.add(kostenField, 1, 2);
        grid.add(new Label("Beschreibung:"), 0, 3);
        grid.add(beschreibungField, 1, 3);
        grid.add(new Label("Typ:"), 0, 4);
        grid.add(typComboBox, 1, 4);

        // Initialize ComboBox
        typComboBox.getItems().addAll(ReparaturTyp.values());

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Add validation
        getDialogPane().lookupButton(ButtonType.OK).addEventFilter(javafx.event.ActionEvent.ACTION, _ -> {
            if (!isInputValid()) {
                throw new IllegalArgumentException("Ungültige Eingabe");
            }
        });

        // Set result converter
        setResultConverter(_ -> {
            if (getResult() == ButtonType.OK) {
                return new ReparaturBuchEintrag(
                    LocalDate.now(),
                    typComboBox.getValue(),
                    beschreibungField.getText(),
                    Double.parseDouble(kostenField.getText()),
                    kennzeichenField.getText(),
                    werkstattField.getText()
                );
            }
            return null;
        });
    }

    private boolean isInputValid() {
        return !kennzeichenField.getText().isEmpty() &&
               !werkstattField.getText().isEmpty() &&
               !kostenField.getText().isEmpty() &&
               typComboBox.getValue() != null;
    }
} 