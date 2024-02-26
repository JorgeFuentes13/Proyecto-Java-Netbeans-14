/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.buk.util;

import cl.buk.model.Empresa;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author joan.toro
 */
public class EmpresaTableModel extends AbstractTableModel{
    
    private final List<Empresa> listaEmpresa;
    private final String[] columnNames = new String[] { "Rut", "Dv", "Razon Social"};
    private final Class[] columnClass = new Class[] {Integer.class, String.class, String.class};
    
    public EmpresaTableModel(List<Empresa> listaEmpresa){
        this.listaEmpresa = listaEmpresa;
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
        return listaEmpresa.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Empresa row = listaEmpresa.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return row.getRut();
            case 1:
                return row.getDv();
            case 2:
                return row.getRazonSocial();
            default:
                break;
        }
        return null;
    }
}
