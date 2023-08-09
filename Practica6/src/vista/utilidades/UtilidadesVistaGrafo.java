/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista.utilidades;

import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;
import javax.swing.JComboBox;
import modelo.Ciudad;

/**
 *
 * @author darkangel
 */
public class UtilidadesVistaGrafo {

    public static void cargarCombo(ListaEnlazada<Ciudad> lista, JComboBox cbx) 
            throws VacioException, PosicionException{
        cbx.removeAllItems();
        
            for (int i = 0; i < lista.size(); i++) {
                cbx.addItem(lista.get(i));
            }
        
    }
    
    public static Ciudad obtenerCombo(ListaEnlazada<Ciudad> lista, JComboBox cbx) 
            throws VacioException, PosicionException {
        return lista.get(cbx.getSelectedIndex());
    }
    
}











