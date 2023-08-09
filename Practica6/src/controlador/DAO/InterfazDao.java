/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.DAO;

import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;
import java.io.IOException;

/**
 *
 * @author darkangel
 */
public interface InterfazDao<T> {
    /**
     * Metodo que permite realizar el guardaddo
     * @param obj Objeto del modelo
     */
    public void guardar(T obj) throws IOException;
    /**
     * Permite modificar los datos en un repositorio de datos
     * @param obj Objeto a modificar
     * @param pos posicion del arreglo
     */
    public void modificar(T obj, Integer pos) throws IOException, VacioException, PosicionException;
    public ListaEnlazada<T> listar();
    public T obtener(Integer id);
}
