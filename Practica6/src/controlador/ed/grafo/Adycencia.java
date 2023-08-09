/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.ed.grafo;

/**
 *
 * @author darkangel
 */
public class Adycencia {
    private Integer destino;
    private double peso;

    public Adycencia(Integer destino, double peso) {
        this.destino = destino;
        this.peso = peso;
    }
    
    public Integer getDestino() {
        return destino;
    }

    public void setDestino(Integer destino) {
        this.destino = destino;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
    
}
