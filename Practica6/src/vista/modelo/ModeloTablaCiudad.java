/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.modelo;

import controlador.ed.lista.ListaEnlazada;
import javax.swing.table.AbstractTableModel;
import modelo.Ciudad;

/**
 *
 * @author wilman
 */
public class ModeloTablaCiudad extends AbstractTableModel{
    ListaEnlazada<Ciudad> lista = new ListaEnlazada<>();

    public ListaEnlazada<Ciudad> getLista() {
        return lista;
    }

    public void setLista(ListaEnlazada<Ciudad> lista) {
        this.lista = lista;
    }

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ciudad ciu = null;
        
        try {
            ciu = lista.get(rowIndex);
        } catch (Exception e) {
        }
        switch (columnIndex) {
            case 0: return (ciu != null) ? ciu.getId() : "No definido";
            case 1: return (ciu != null) ? ciu.getNombre() : "No definido";
                
            default:
                return null;
        }
    }
    
    public String getColumnName(int column){
        switch (column) {
            case 0: return "ID";
            case 1: return "NOMBRE";
                
            default:
                return null;
        }
    }
    
}
