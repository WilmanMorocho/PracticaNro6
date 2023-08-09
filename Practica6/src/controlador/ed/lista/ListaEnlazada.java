/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.ed.lista;

import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;

/**
 *
 * @author darkangel
 */
public class ListaEnlazada<E> {

    private NodoLista<E> cabecera;//8  --->   5  --->   7 ---> 9 ---> null
    private Integer size = 0;

    public NodoLista getCabecera() {
        return cabecera;
    }

    public void setCabecera(NodoLista cabecera) {
        this.cabecera = cabecera;
    }

    public boolean isEmpty() {
        
        return cabecera == null;

    }

    public void insertar(E info) {
        NodoLista<E> nuevo = new NodoLista<>(info, null);
        if (isEmpty()) {
            this.cabecera = nuevo;
            this.size++;
        } else {
            NodoLista<E> aux = cabecera;
            /*for (int i = 0; i < size()-1; i++) {
                aux = aux.getSig();
            }*/
            //7   ----  8  ----  9   --- null     0
            //7  -----  8 ----- 9  --- 0  --- null
            while (aux.getSig() != null) {
                aux = aux.getSig();
            }
            aux.setSig(nuevo);
            this.size++;
        }

    }

    public void insertarInicio(E info) {
        //7  -----  8 ----- 9  --- 0  --- null
        ///10
        //10  --- 7  -----  8 ----- 9  --- 0  --- null
        if (isEmpty()) {
            insertar(info);
        } else {
            NodoLista<E> nuevo = new NodoLista<>(info, null);
            nuevo.setSig(cabecera);
            cabecera = nuevo;
            size++;
        }
    }

    public void insertarPosicion(E info, Integer pos) throws PosicionException {
        //7  -----  8 ----- 9  --- 0  --- null
        //dato = 3  -- pos 1
        //7  -----  8 ----- 9  --- 0  --- null
        //7  -----   3  ----  8 ----- 9  --- 0  --- null
        if (isEmpty()) {
            insertar(info);
        } else if (pos >= 0 && pos < size() && pos == 0) {
            insertarInicio(info);
        } else if (pos >= 0 && pos < size()) {
            NodoLista<E> nuevo = new NodoLista<>(info, null);
            NodoLista<E> aux = cabecera;
            for (int i = 0; i < (pos - 1); i++) {
                aux = aux.getSig();
            }
            NodoLista<E> sig = aux.getSig();
            aux.setSig(nuevo);
            nuevo.setSig(sig);
            size++;
        } else {
            throw new PosicionException();
        }
    }
    
    public void set(int pos, E nuevoValor) throws VacioException, PosicionException {
        if (isEmpty()) {
            throw new VacioException();
        } else {
            if (pos >= 0 && pos < size()) {
                if (pos == 0) {
                    cabecera.setInfo(nuevoValor);
                } else {
                    NodoLista<E> aux = cabecera;
                    for (int i = 0; i < pos; i++) {
                        aux = aux.getSig();
                    }
                    aux.setInfo(nuevoValor);
                }
            } else {
                throw new PosicionException();
            }
        }
    }

    public E get(Integer pos) throws VacioException, PosicionException {
        //7  -----  8 ----- 9  --- 0  --- null
        if (isEmpty()) {
            throw new VacioException();
        } else {
            E dato = null;
            if (pos >= 0 && pos < size()) {
                if (pos == 0) {
                    dato = cabecera.getInfo();
                } else {
                    NodoLista<E> aux = cabecera;
                    for (int i = 0; i < pos; i++) {
                        aux = aux.getSig();
                    }
                    dato = aux.getInfo();
                }
            } else {
                throw new PosicionException();
            }
            return dato;
        }
    }
    
    public void update(Integer pos, E dato) throws VacioException, PosicionException {
        if (isEmpty()) {
            throw new VacioException();
        } else {
            
            if (pos >= 0 && pos < size()) {
                if (pos == 0) {
                    //dato = cabecera.getInfo();
                    cabecera.setInfo(dato);
                } else {
                    NodoLista<E> aux = cabecera;
                    for (int i = 0; i < pos; i++) {
                        aux = aux.getSig();
                    }
                    aux.setInfo(dato);
                }
            } else {
                throw new PosicionException();
            }
            //return dato;
        }
    }
    
    public E delete(Integer pos) throws VacioException, PosicionException {
        //7  -----  8 ----- 9  --- 0  --- null
        //delete 8
        //7  -----   9  --- 0  --- null
        if (isEmpty()) {
            throw new VacioException();
        } else {
            E dato = null;
            if (pos >= 0 && pos < size()) {
                if (pos == 0) {
                    dato = cabecera.getInfo();
                    cabecera = cabecera.getSig();
                    size--;
                } else {
                    NodoLista<E> aux = cabecera;
                    for (int i = 0; i < (pos-1); i++) {
                        aux = aux.getSig();
                    }
                    
                    //if((pos + 1) == size()) {
                        NodoLista<E> aux1 = aux.getSig();
                       dato = aux1.getInfo();
                    //} else {
                     //   dato = aux.getInfo();
                    //}
                    NodoLista<E> proximo = aux.getSig();
                        aux.setSig(proximo.getSig());
                    size--;
                }
            } else {
                throw new PosicionException();
            }
            return dato;
        }
    }

    public Integer size() {
        return size;
    }

    public void imprimir() throws VacioException {
        if (isEmpty()) {
            throw new VacioException();
        } else {
            NodoLista<E> aux = cabecera;
            System.out.println("----LISTA------");
            for (int i = 0; i < size(); i++) {
                System.out.print(aux.getInfo() + "  ");
                aux = aux.getSig();
            }
            System.out.println("");
            System.out.println("----LISTA FIN------");
        }

    }
    
    public void deleteAll() {
        this.cabecera = null;
        this.size = 0;
    }
    
    public E[] toArray() {
        
        E[] matriz = null;
        if(this.size > 0) {
            matriz = (E[]) java.lang.reflect.Array.newInstance(cabecera.getInfo().getClass(), this.size);
            NodoLista<E> aux = cabecera;
            for(int i = 0; i < this.size; i++) {
                matriz[i] = aux.getInfo();
                aux = aux.getSig();
            }
        }
        return matriz;
    }
    
    public ListaEnlazada<E> toList(E[] matriz) {
        this.deleteAll();
        for(int i = 0; i < matriz.length; i++) {
                this.insertar(matriz[i]);
        }
        return this;
    }

}
