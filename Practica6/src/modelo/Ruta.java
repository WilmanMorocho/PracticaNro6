/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author wilman
 */
public class Ruta {

    private Integer idCiudadOri;
    private String nomCiudadDes;
    private Double distancia;
    
    public Ruta(int idCiudadOri, String nomCiudadDes, double distancia) {
        this.idCiudadOri = idCiudadOri;
        this.nomCiudadDes = nomCiudadDes;
        this.distancia = distancia;
    }

    public Ruta() {
        
    }

    public Integer getIdCiudadOri() {
        return idCiudadOri;
    }

    public void setIdCiudadOri(Integer idCiudadOri) {
        this.idCiudadOri = idCiudadOri;
    }

    public String getNomCiudadDes() {
        return nomCiudadDes;
    }

    public void setNomCiudadDes(String nomCiudadDes) {
        this.nomCiudadDes = nomCiudadDes;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

}
