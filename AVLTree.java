import java.util.ArrayList;
import java.util.List;

public class AVLTree {
    private Nodo raiz;
    private int contadorRotaciones;
    private List<String> registroRotaciones;
    private int contadorComparaciones;

    public AVLTree() {
        this.raiz = null;
        this.contadorRotaciones = 0;
        this.registroRotaciones = new ArrayList<>();
        this.contadorComparaciones = 0;
    }

    private int altura(Nodo nodo) {
        if (nodo == null) return 0;
        return nodo.altura;
    }

    private int factorEquilibrio(Nodo nodo) {
        if (nodo == null) return 0;
        return altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    private void actualizarAltura(Nodo nodo) {
        if (nodo != null) {
            nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
        }
    }

    /*
     * Rotacion Derecha (LL)
     *
     * Antes:          Despues:
     *     A              B
     *    /              / \
     *   B              C   A
     *  /
     * C
     */
    private Nodo rotacionDerecha(Nodo y) {
        Nodo x = y.izquierdo;
        Nodo T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        actualizarAltura(y);
        actualizarAltura(x);

        return x;
    }

    /*
     * Rotacion Izquierda (RR)
     *
     * Antes:          Despues:
     *     A              B
     *      \            / \
     *       B          A   C
     *        \
     *         C
     */
    private Nodo rotacionIzquierda(Nodo x) {
        Nodo y = x.derecho;
        Nodo T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        actualizarAltura(x);
        actualizarAltura(y);

        return y;
    }

    public void insertar(int valor) {
        raiz = insertarRecursivo(raiz, valor);
    }

    private Nodo insertarRecursivo(Nodo nodo, int valor) {
        if (nodo == null) {
            return new Nodo(valor);
        }

        if (valor < nodo.valor) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, valor);
        } else if (valor > nodo.valor) {
            nodo.derecho = insertarRecursivo(nodo.derecho, valor);
        } else {
            return nodo;
        }

        actualizarAltura(nodo);

        int fe = factorEquilibrio(nodo);

        // CASO 1: Rotacion LL
        if (fe > 1 && valor < nodo.izquierdo.valor) {
            contadorRotaciones++;
            registroRotaciones.add("LL");
            return rotacionDerecha(nodo);
        }

        // CASO 2: Rotacion RR
        if (fe < -1 && valor > nodo.derecho.valor) {
            contadorRotaciones++;
            registroRotaciones.add("RR");
            return rotacionIzquierda(nodo);
        }

        // CASO 3: Rotacion LR (doble)
        if (fe > 1 && valor > nodo.izquierdo.valor) {
            contadorRotaciones++;
            registroRotaciones.add("LR");
            nodo.izquierdo = rotacionIzquierda(nodo.izquierdo);
            return rotacionDerecha(nodo);
        }

        // CASO 4: Rotacion RL (doble)
        if (fe < -1 && valor < nodo.derecho.valor) {
            contadorRotaciones++;
            registroRotaciones.add("RL");
            nodo.derecho = rotacionDerecha(nodo.derecho);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    public void eliminar(int valor) {
        raiz = eliminarRecursivo(raiz, valor);
    }

    private Nodo eliminarRecursivo(Nodo nodo, int valor) {
        if (nodo == null) {
            return null;
        }

        if (valor < nodo.valor) {
            nodo.izquierdo = eliminarRecursivo(nodo.izquierdo, valor);
        } else if (valor > nodo.valor) {
            nodo.derecho = eliminarRecursivo(nodo.derecho, valor);
        } else {
            // CASO 1: Nodo hoja
            if (nodo.izquierdo == null && nodo.derecho == null) {
                return null;
            }

            // CASO 2: Nodo con un hijo
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            }
            if (nodo.derecho == null) {
                return nodo.izquierdo;
            }

            // CASO 3: Nodo con dos hijos
            Nodo sucesor = encontrarMinimo(nodo.derecho);
            nodo.valor = sucesor.valor;
            nodo.derecho = eliminarRecursivo(nodo.derecho, sucesor.valor);
        }

        if (nodo == null) {
            return null;
        }

        actualizarAltura(nodo);

        return rebalancear(nodo);
    }

    private Nodo rebalancear(Nodo nodo) {
        int fe = factorEquilibrio(nodo);

        // Rotacion LL
        if (fe > 1 && factorEquilibrio(nodo.izquierdo) >= 0) {
            contadorRotaciones++;
            registroRotaciones.add("LL (eliminacion)");
            return rotacionDerecha(nodo);
        }

        // Rotacion LR
        if (fe > 1 && factorEquilibrio(nodo.izquierdo) < 0) {
            contadorRotaciones++;
            registroRotaciones.add("LR (eliminacion)");
            nodo.izquierdo = rotacionIzquierda(nodo.izquierdo);
            return rotacionDerecha(nodo);
        }

        // Rotacion RR
        if (fe < -1 && factorEquilibrio(nodo.derecho) <= 0) {
            contadorRotaciones++;
            registroRotaciones.add("RR (eliminacion)");
            return rotacionIzquierda(nodo);
        }

        // Rotacion RL
        if (fe < -1 && factorEquilibrio(nodo.derecho) > 0) {
            contadorRotaciones++;
            registroRotaciones.add("RL (eliminacion)");
            nodo.derecho = rotacionDerecha(nodo.derecho);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    private Nodo encontrarMinimo(Nodo nodo) {
        Nodo actual = nodo;
        while (actual.izquierdo != null) {
            actual = actual.izquierdo;
        }
        return actual;
    }

    public boolean buscar(int valor) {
        contadorComparaciones = 0;
        return buscarRecursivo(raiz, valor);
    }

    private boolean buscarRecursivo(Nodo nodo, int valor) {
        contadorComparaciones++;
        if (nodo == null) return false;
        if (valor == nodo.valor) return true;
        if (valor < nodo.valor) {
            return buscarRecursivo(nodo.izquierdo, valor);
        } else {
            return buscarRecursivo(nodo.derecho, valor);
        }
    }

    public List<Integer> buscarConCamino(int valor) {
        contadorComparaciones = 0;
        List<Integer> camino = new ArrayList<>();
        buscarConCaminoRecursivo(raiz, valor, camino);
        return camino;
    }

    private boolean buscarConCaminoRecursivo(Nodo nodo, int valor, List<Integer> camino) {
        contadorComparaciones++;
        if (nodo == null) return false;
        camino.add(nodo.valor);
        if (valor == nodo.valor) return true;
        if (valor < nodo.valor) {
            return buscarConCaminoRecursivo(nodo.izquierdo, valor, camino);
        } else {
            return buscarConCaminoRecursivo(nodo.derecho, valor, camino);
        }
    }

    public int getContadorComparaciones() {
        return contadorComparaciones;
    }

    public int getAltura() {
        return altura(raiz);
    }

    public int getFactorEquilibrioRaiz() {
        return factorEquilibrio(raiz);
    }

    public int getContadorRotaciones() {
        return contadorRotaciones;
    }

    public List<String> getRegistroRotaciones() {
        return registroRotaciones;
    }

    public List<Integer> inorden() {
        List<Integer> resultado = new ArrayList<>();
        inordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void inordenRecursivo(Nodo nodo, List<Integer> resultado) {
        if (nodo != null) {
            inordenRecursivo(nodo.izquierdo, resultado);
            resultado.add(nodo.valor);
            inordenRecursivo(nodo.derecho, resultado);
        }
    }

    public List<Integer> preorden() {
        List<Integer> resultado = new ArrayList<>();
        preordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void preordenRecursivo(Nodo nodo, List<Integer> resultado) {
        if (nodo != null) {
            resultado.add(nodo.valor);
            preordenRecursivo(nodo.izquierdo, resultado);
            preordenRecursivo(nodo.derecho, resultado);
        }
    }

    public List<Integer> postorden() {
        List<Integer> resultado = new ArrayList<>();
        postordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void postordenRecursivo(Nodo nodo, List<Integer> resultado) {
        if (nodo != null) {
            postordenRecursivo(nodo.izquierdo, resultado);
            postordenRecursivo(nodo.derecho, resultado);
            resultado.add(nodo.valor);
        }
    }
}