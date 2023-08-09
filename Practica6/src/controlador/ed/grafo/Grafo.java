/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.ed.grafo;

import controlador.ed.grafo.exception.GrafoSizeExeption;
import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;

/**
 *
 * @author darkangel
 */
public abstract class Grafo {
    public abstract Integer numVertices();
    public abstract Integer numAristas();
    public abstract Boolean existeArista(Integer i, Integer j) throws GrafoSizeExeption ;
    public abstract Double pesoArista(Integer i, Integer j) throws GrafoSizeExeption;
    //1 ----- 3
    //3 ----- 1
    public abstract void insertar(Integer i, Integer j) throws GrafoSizeExeption;
    //1 ----- 3 -->peso 5
    public abstract void insertar(Integer i, Integer j, Double peso) throws GrafoSizeExeption;
    public abstract ListaEnlazada<Adycencia> adycentes(Integer i);

    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder("GRAFO"+"\n");
        for(int i = 1; i <= numVertices(); i++){
            grafo.append(" V "+i+"\n");
            ListaEnlazada<Adycencia> lista = adycentes(i);
            grafo.append((!lista.isEmpty())? "Adycencias":"No Adycencias");
            grafo.append("\n");
            for(int j = 0; j < lista.size();j++) {
                try {
                    Adycencia aux = lista.get(j);                    
                    grafo.append(" -- V "+aux.getDestino()+" PESO --> "+aux.getPeso()+"\n");
                } catch (Exception e) {
                }
            }
        }
        return grafo.toString();
    }
    
    public ListaEnlazada camin0(Integer i, Integer d) throws VacioException, PosicionException {
        ListaEnlazada camino = new ListaEnlazada();
        System.out.println(i+"   "+d);
        if(estaConectado()) {            
            ListaEnlazada pesos = new ListaEnlazada();
            Boolean finalizar = false;
            Integer inicial = i;
            camino.insertar(i);
            while (!finalizar) {                
                ListaEnlazada<Adycencia> adycencias = adycentes(inicial);
                Double peso = Double.MAX_VALUE;
                //System.out.println(peso);
                int T = -1;//vertice destino
                for(int j = 0; j < adycencias.size(); j++ ) {
                    Adycencia ad = adycencias.get(j);
                    
                    if(!estaCamino(camino, ad.getDestino())) {
                        //System.out.println("PASO CAMINO");
                        Double pesoArista = ad.getPeso();
                        if(d.intValue() == ad.getDestino().intValue()) {
                            T = ad.getDestino();
                            peso = pesoArista;
                            break;
                        } else if (pesoArista < peso){//camino minimo (pesoArista < peso) ---- camino maximo (pesoArista > peso)
                            T = ad.getDestino();
                            peso = pesoArista;
                        }
                    } 
                    //System.out.println("PASO CAMINO --");
                }
                if(T == -1) {
                    System.out.println("PASO POR AQUI vacio");
                    camino.deleteAll();
                    break;
                }                    
                pesos.insertar(peso);
                camino.insertar(T);
                inicial = T;
                if(d.intValue() == inicial.intValue()) {
                    finalizar = true;
                }
            }
        }
        return camino;
    }
    
    private Boolean estaCamino(ListaEnlazada<Integer> lista, Integer vertice) throws VacioException, PosicionException {
        Boolean band = false;
        for(int i = 0; i < lista.size(); i++) {
        if(lista.get(i).intValue() == vertice.intValue()) {
            band = true;
            break;
        }
    }
        return band;
    }
    
    
    private Boolean estaConectado() {
        Boolean band = true;
        for(int i = 1; i <= numVertices(); i++) {
            ListaEnlazada<Adycencia> lista = adycentes(i);
            if(lista.size() == 0) {
                band = false;
                break;
            }
        }
        return band;
    }
    
    
}








