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
public class DepartamentoTableModel extends AbstractTableModel{
    
    private final List<Departamento> listaDepartamento;
    private final String[] columnNames = new String[] {"Id", "Nombre", "Sigla"};
    private final Class[] columnClass = new Class[] {Integer.class, String.class, String.class};
    
    public DepartamentoTableModel(List<Departamento> listaDepartamento){
        this.listaDepartamento = listaDepartamento;
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
        return listaDepartamento.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Departamento row = listaDepartamento.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return row.getId();
            case 1:
                return row.getNombre();
            case 2:
                return row.getSigla();
            default:
                break;
        }
        return null;
    }
}
