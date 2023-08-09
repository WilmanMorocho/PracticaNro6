/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.ed.grafo.busqueda;

import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;
import modelo.Ciudad;
import modelo.Ruta;

/**
 *
 * @author Baller 293
 */
public class Floyd {

    public static ListaEnlazada<String> encontrarRutaMasCorta(ListaEnlazada<Ciudad> ciudades, ListaEnlazada<Ruta> rutas,
            Ciudad ciudadOrigen, Ciudad ciudadDestino) throws VacioException, PosicionException {
        Integer numCiudades = ciudades.size();

        // Crear matriz de distancias iniciales
        ListaEnlazada<ListaEnlazada<Double>> distancias = inicializarMatrizDistancias(ciudades, rutas);

        // Inicializar matriz de rutas intermedias
        ListaEnlazada<ListaEnlazada<Integer>> rutasIntermedias = inicializarMatrizRutasIntermedias(numCiudades);

        // Calcular distancias mínimas y rutas intermedias
        calcularDistanciasRutas(distancias, rutasIntermedias);

        // Encontrar los índices de las ciudades de origen y destino
        int indiceOrigen = encontrarIndiceCiudad(ciudades, ciudadOrigen);
        int indiceDestino = encontrarIndiceCiudad(ciudades, ciudadDestino);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            ListaEnlazada<String> rutaVacia = new ListaEnlazada<>();
            rutaVacia.insertar("Ciudad de origen o destino no encontrada en la lista");
            return rutaVacia;
        }

        return construirRuta(ciudades, rutasIntermedias, indiceOrigen, indiceDestino);
    }

    private static ListaEnlazada<ListaEnlazada<Double>> inicializarMatrizDistancias(ListaEnlazada<Ciudad> ciudades,
            ListaEnlazada<Ruta> rutas) throws VacioException, PosicionException {
        int numCiudades = ciudades.size();
        ListaEnlazada<ListaEnlazada<Double>> distancias = new ListaEnlazada<>();

        for (int i = 0; i < numCiudades; i++) {
            ListaEnlazada<Double> filaDistancias = new ListaEnlazada<>();
            for (int j = 0; j < numCiudades; j++) {
                if (i == j) {
                    filaDistancias.insertar(0.0);
                } else {
                    filaDistancias.insertar(Double.POSITIVE_INFINITY);
                }
            }
            distancias.insertar(filaDistancias);
        }

        for (Ruta ruta : rutas.toArray()) {
            int ciudadOrigen = ruta.getIdCiudadOri() - 1;
            int ciudadDestino = -1;
            for (int i = 0; i < ciudades.size(); i++) {
                if (ciudades.get(i).getNombre().equals(ruta.getNomCiudadDes())) {
                    ciudadDestino = i;
                    break;
                }
            }
            if (ciudadDestino != -1) {
                distancias.get(ciudadOrigen).set(ciudadDestino, ruta.getDistancia());
            }
        }

        return distancias;
    }

    private static int encontrarIndiceCiudad(ListaEnlazada<Ciudad> ciudades, Ciudad ciudad) throws VacioException, PosicionException {
        for (int i = 0; i < ciudades.size(); i++) {
            if (ciudades.get(i).equals(ciudad)) {
                return i;
            }
        }
        return -1; // Si no se encuentra la ciudad en la lista
    }

    private static ListaEnlazada<ListaEnlazada<Integer>> inicializarMatrizRutasIntermedias(int numCiudades) throws VacioException, PosicionException {
        ListaEnlazada<ListaEnlazada<Integer>> rutasIntermedias = new ListaEnlazada<>();
        for (int i = 0; i < numCiudades; i++) {
            rutasIntermedias.insertar(new ListaEnlazada<>());
            for (int j = 0; j < numCiudades; j++) {
                rutasIntermedias.get(i).insertar(-1);
            }
        }
        return rutasIntermedias;
    }

    private static void calcularDistanciasRutas(ListaEnlazada<ListaEnlazada<Double>> distancias,
            ListaEnlazada<ListaEnlazada<Integer>> rutasIntermedias) throws VacioException, PosicionException {
        int numCiudades = distancias.size();
        for (int k = 0; k < numCiudades; k++) {
            for (int i = 0; i < numCiudades; i++) {
                for (int j = 0; j < numCiudades; j++) {
                    double distanciaIK = distancias.get(i).get(k);
                    double distanciaKJ = distancias.get(k).get(j);
                    if (distanciaIK + distanciaKJ < distancias.get(i).get(j)) {
                        distancias.get(i).update(j, distanciaIK + distanciaKJ);
                        rutasIntermedias.get(i).update(j, k);
                    }
                }
            }
        }
    }

    private static ListaEnlazada<String> construirRuta(ListaEnlazada<Ciudad> ciudades,
            ListaEnlazada<ListaEnlazada<Integer>> rutasIntermedias,
            int ciudadOrigen, int ciudadDestino) throws VacioException, PosicionException {
        ListaEnlazada<String> ruta = new ListaEnlazada<>();
        if (rutasIntermedias.get(ciudadOrigen).get(ciudadDestino) == 1) {
            ruta.insertar("No hay ruta disponible");
        } else {
            construirRutaRecursiva(ruta, ciudades, rutasIntermedias, ciudadOrigen - 1, ciudadDestino - 1);
        }
        return ruta;
    }

    private static void construirRutaRecursiva(ListaEnlazada<String> ruta, ListaEnlazada<Ciudad> ciudades,
            ListaEnlazada<ListaEnlazada<Integer>> rutasIntermedias,
            int ciudadOrigen, int ciudadDestino) throws VacioException, PosicionException {
        int ciudadIntermedia = rutasIntermedias.get(ciudadOrigen).get(ciudadDestino);
        if (ciudadIntermedia == -1) {
            ruta.insertar(ciudades.get(ciudadDestino).getNombre());
        } else {
            construirRutaRecursiva(ruta, ciudades, rutasIntermedias, ciudadOrigen, ciudadIntermedia);
            construirRutaRecursiva(ruta, ciudades, rutasIntermedias, ciudadIntermedia, ciudadDestino);
        }
    }
}
