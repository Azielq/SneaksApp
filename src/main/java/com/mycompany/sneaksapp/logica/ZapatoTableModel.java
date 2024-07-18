package com.mycompany.sneaksapp.logica;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ZapatoTableModel extends AbstractTableModel {

    private List<Zapato> zapatos;
    private final String[] columnNames = {"ID", "Brand", "Model", "Size", "Color", "Location", "Price $", "Image"};
    private boolean allEditable = true;

    public ZapatoTableModel(List<Zapato> zapatos) {
    this.zapatos = (zapatos != null) ? zapatos : new ArrayList<>();
}
    
    public ZapatoTableModel() {
    
    }
    
    public void setAllEditable(boolean editable) {
        this.allEditable = editable; // Método para ajustar la editabilidad de todas las celdas
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (!allEditable) {
            return false; // Si `allEditable` es false, ninguna celda es editable
        }
        return columnIndex != 0; // Solo la primera columna no es editable
    }
    
    public void setData(List<Zapato> nuevosZapatos) {
        this.zapatos = nuevosZapatos; // Actualiza la lista de zapatos
        fireTableDataChanged(); // Notifica a la tabla que los datos han cambiado
    }

    public int getZapatosCount() {
        return zapatos.size();
    }
    
    
    public Zapato getZapatoAt(int rowIndex) {
        return zapatos.get(rowIndex);
    }
    
    @Override
    public int getRowCount() {
        return zapatos.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    

    @Override
    public void setValueAt(Object value, int row, int col) {
        Zapato zapato = zapatos.get(row);
    switch (col) {
        case 1 -> zapato.setBrand((String) value);
        case 2 -> zapato.setModel((String) value);
        case 3 -> zapato.setSize(Byte.parseByte((String) value));
        case 4 -> zapato.setColor((String) value);
        case 5 -> zapato.setUbication((String) value);
        case 6 -> zapato.setPrice(Integer.parseInt((String) value));
        case 7 -> zapato.setImage((String) value);
        }
        fireTableCellUpdated(row, col);
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    // Métodos para añadir y eliminar zapatos de la lista
    public void addZapato(Zapato zapato) {
        zapatos.add(zapato);
        fireTableRowsInserted(zapatos.size() - 1, zapatos.size() - 1);
    }

    public void removeZapato(int rowIndex) {
        zapatos.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void removeRowsByIds(List<Integer> ids) {
        for (int i = zapatos.size() - 1; i >= 0; i--) {
            if (ids.contains(zapatos.get(i).getShoeid())) {
                zapatos.remove(i);
            }
        }
        fireTableDataChanged(); // Notifica a la tabla que los datos han cambiado
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Zapato zapato = zapatos.get(rowIndex);
        return switch (columnIndex) 
        {
            case 0 -> zapato.getShoeid();
            case 1 -> zapato.getBrand();
            case 2 -> zapato.getModel();
            case 3 -> zapato.getSize();
            case 4 -> zapato.getColor();
            case 5 -> zapato.getUbication();
            case 6 -> zapato.getPrice();
            case 7 -> zapato.getImage();
            default -> null;
        };
    }

    
}



