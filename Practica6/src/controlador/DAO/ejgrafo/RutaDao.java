/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.DAO.ejgrafo;

import controlador.DAO.AdaptadorDao;
import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;
import java.io.IOException;
import modelo.Ruta;

/**
 *
 * @author wilman
 */
public class RutaDao extends AdaptadorDao<Ruta>{
    private Ruta ruta;

    public RutaDao() {
        super(Ruta.class);
    }

    public Ruta getRuta() {
        if (ruta == null) {
            ruta = new Ruta(); // Llama al constructor sin argumentos de Ruta
        }
        return ruta;
    }

    public void setRuta(int idCiudadOri, String nomCiudadDes, double distancia) {
        ruta = new Ruta(idCiudadOri, nomCiudadDes, distancia); // Llama al constructor con argumentos de Ruta
    }
    
    public void guardar() throws IOException {
        this.guardar(ruta);
    }
    
    public ListaEnlazada<Ruta> listarPorCiudad(Integer id) throws VacioException, PosicionException {
        ListaEnlazada<Ruta> lista = new ListaEnlazada<>();
        ListaEnlazada<Ruta> listado = listar();
        
        for (int i = 0; i < listado.size(); i++) {
            Ruta aux = listado.get(i);
            if (aux.getIdCiudadOri().equals(id)) {
                lista.insertar(aux);
            }
        }
        return lista;
    }
    
}
