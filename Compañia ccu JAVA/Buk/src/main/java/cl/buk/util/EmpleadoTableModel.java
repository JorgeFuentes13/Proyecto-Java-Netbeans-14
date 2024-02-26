/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.buk.util;

import cl.buk.model.*;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author joan.toro
 */
public class EmpleadoTableModel extends AbstractTableModel{
    
    private final List<Empleado> listaEmpleado;
    private final String[] columnNames = new String[] { "Rut", "Nombre Completo", "Cargo", "Salario"};
    private final Class[] columnClass = new Class[] {String.class, String.class, String.class, Integer.class};
    
    public EmpleadoTableModel(List<Empleado> listaEmpleado){
        this.listaEmpleado = listaEmpleado;
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
        return listaEmpleado.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Empleado row = listaEmpleado.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return row.getRut() + "-" + row.getDv();
            case 1:
                return row.getNombre() + " " + row.getApellidos();
            case 2:
                return row.getCargo();
            case 3:
                return row.getSalario();
            default:
                break;
        }
        return null;
    }
}
