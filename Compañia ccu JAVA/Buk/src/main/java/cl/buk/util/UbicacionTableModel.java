/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.buk.util;

import cl.buk.model.Ubicacion;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author joan.toro
 */
public class UbicacionTableModel extends AbstractTableModel{
    
    private final List<Ubicacion> listaUbicacion;
    private final String[] columnNames = new String[] { "Calle", "Numero", "Provincia", "Region"};
    private final Class[] columnClass = new Class[] {String.class, Integer.class, String.class, String.class};
    
    public UbicacionTableModel(List<Ubicacion> listaUbicacion){
        this.listaUbicacion = listaUbicacion;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex){
        return columnClass[columnIndex];
    }
    
    @Override
    public int getRowCount() {
        return listaUbicacion.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ubicacion row = listaUbicacion.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return row.getCalle();
            case 1:
                return row.getNumero();
            case 2:
                return row.getProvincia();
            case 3:
                return row.getRegion();
            default:
                break;
        }
        return null;
    }
}
