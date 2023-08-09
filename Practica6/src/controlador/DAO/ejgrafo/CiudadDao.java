/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.DAO.ejgrafo;

import controlador.DAO.AdaptadorDao;
import java.io.IOException;
import modelo.Ciudad;

/**
 *
 * @author wilman
 */
public class CiudadDao extends AdaptadorDao<Ciudad>{
    private Ciudad ciudad;

    public CiudadDao() {
        super(Ciudad.class);
    }

    public Ciudad getCiudad() {
        if(ciudad == null){
            ciudad = new Ciudad();
        }
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
    
    public void guardar() throws IOException{
        ciudad.setId(generateId());
        this.guardar(ciudad);
    }
    
    private Integer generateId(){
        return listar().size() + 1;
    }
    
    
    
    
}
