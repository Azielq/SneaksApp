package com.mycompany.sneaksapp.logica;

import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;

public class IntegerEditor extends DefaultCellEditor {
    private final JTextField textField;

    public IntegerEditor() {
        super(new JTextField());
        this.textField = (JTextField) getComponent();

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateInput();
            }
        });
    }

     @Override
    public boolean stopCellEditing() {
        try {
            Integer.parseInt(textField.getText());
            return super.stopCellEditing();
        } catch (NumberFormatException e) {
            textField.setBorder(BorderFactory.createLineBorder(Color.red));
            JOptionPane.showMessageDialog(null, "Invalid input: Please enter an integer value.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            textField.setBorder(BorderFactory.createLineBorder(Color.black));
            // No finalizar la edición si la entrada no es válida
            return false;
        }
    }

    private boolean validateInput() {
        try {
            Integer.parseInt(textField.getText());
            return true; // La entrada es válida, continua con la edición
        } catch (NumberFormatException e) {
            //JOptionPane.showMessageDialog(null, "Por favor, introduce un número válido.", "Error de formato", JOptionPane.WARNING_MESSAGE);
            textField.setText(""); // Limpiar el campo para forzar al usuario a ingresar un valor válido
            return false; // La entrada no es válida, no finalizar la edición
        }
    }
}