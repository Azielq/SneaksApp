package com.mycompany.sneaksapp.logica;

import java.awt.Component;
import javax.swing.*;
import java.awt.event.*;

public class DynamicComboBoxEditor extends DefaultCellEditor {
    private JComboBox<String> column3ComboBox;
    private DefaultComboBoxModel<String> column3Model;
    private JTable table;

    public DynamicComboBoxEditor(JComboBox<String> comboBox, JTable table) {
        super(comboBox);
        this.column3ComboBox = new JComboBox<>();
        this.column3Model = new DefaultComboBoxModel<>();
        this.column3ComboBox.setModel(column3Model);
        this.table = table;

        column3ComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped(); // Notifica que la edición ha finalizado, lo que desencadena getCellEditorValue()
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return column3ComboBox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Object selectedValue = table.getValueAt(row, 1);
        column3Model.removeAllElements();

        if ("Nike".equals(selectedValue)) {
            column3Model.addElement("Jordan 1");
            column3Model.addElement("Jordan 2");
            column3Model.addElement("Airforce 1");
            column3Model.addElement("Dunk High");
        } else if ("Adidas".equals(selectedValue)) {
            column3Model.addElement("Samba");
            column3Model.addElement("SuperStar");
            column3Model.addElement("StanSmith");
            column3Model.addElement("Yeezy");
        }

        column3ComboBox.setSelectedItem(value); // Establece el valor actual como seleccionado en el JComboBox
        return column3ComboBox;
    }
}