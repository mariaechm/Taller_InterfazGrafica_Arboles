package arbolBB;

import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author Isauro Rivera., Josiibel Perez, Maria Chuico
 */
public class ArbolBB {

    private Nodo raiz;
    int num_nodos;
    int alt;

    public ArbolBB() {
        raiz = null;
    }

    public boolean agregar(int dato) {
        Nodo nuevo = new Nodo(dato, null, null);
        insertar(nuevo, raiz);
        return true;
    }

    // Metodo para insertar un dato en el arbol... no acepta repetidos :)
    public void insertar(Nodo nuevo, Nodo pivote) {
        if (this.raiz == null) {
            raiz = nuevo;
        } else {
            if (nuevo.getDato() <= pivote.getDato()) {
                if (pivote.getIzq() == null) {
                    pivote.setIzq(nuevo);
                } else {
                    insertar(nuevo, pivote.getIzq());
                }
            } else {
                if (pivote.getDer() == null) {
                    pivote.setDer(nuevo);
                } else {
                    insertar(nuevo, pivote.getDer());
                }
            }
        }
    }

    public Nodo getRaiz() {
        return raiz;
    }

    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
    }

    // Recorrido preorden, recibe el nodo a empezar (raiz) y una linkedlist para ir guardando el recorrido
    public LinkedList preOrden() {
        LinkedList rec = new LinkedList();
        preorden(raiz, rec);
        return rec;
    }

    public void preorden(Nodo aux, LinkedList recorrido) {
        if (aux != null) {
            recorrido.add(aux.getDato());
            preorden(aux.getIzq(), recorrido);
            preorden(aux.getDer(), recorrido);
        }
    }

    // Recorrido inorden, recibe el nodo a empezar (raiz) y una linkedlist para ir guardando el recorrido
    public LinkedList inOrden() {
        LinkedList rec = new LinkedList();
        inorden(raiz, rec);
        return rec;
    }

    public void inorden(Nodo aux, LinkedList recorrido) {
        if (aux != null) {
            inorden(aux.getIzq(), recorrido);
            recorrido.add(aux.getDato());
            inorden(aux.getDer(), recorrido);
        }
    }

    // Recorrido postorden, recibe el nodo a empezar (raiz) y una linkedlist para ir guardando el recorrido
    public LinkedList postOrden() {
        LinkedList rec = new LinkedList();
        postorden(raiz, rec);
        return rec;
    }

    public void postorden(Nodo aux, LinkedList recorrido) {
        if (aux != null) {
            postorden(aux.getIzq(), recorrido);
            postorden(aux.getDer(), recorrido);
            recorrido.add(aux.getDato());
        }
    }

    // Metodo para verificar si hay un nodo en el arbol
    public boolean existe(int dato) {
        Nodo aux = raiz;
        while (aux != null) {
            if (dato == aux.getDato()) {
                return true;
            } else if (dato > aux.getDato()) {
                aux = aux.getDer();
            } else {
                aux = aux.getIzq();
            }
        }
        return false;
    }

    private void altura(Nodo aux, int nivel) {
        if (aux != null) {
            altura(aux.getIzq(), nivel + 1);
            alt = nivel;
            altura(aux.getDer(), nivel + 1);
        }
    }

    // Devuelve la altura del arbol
    public int getAltura() {
        altura(raiz, 1);
        return alt;
    }

    public JPanel getdibujo() {
        return new ArbolExpresionGrafico(this);
    }

    // Metodo para eliminar un nodo del arbol
    public boolean eliminar(int dato) {
        Nodo aux = raiz;
        Nodo padre = null;
        while (aux != null && aux.getDato() != dato) {
            padre = aux;
            if (dato < aux.getDato()) {
                aux = aux.getIzq();
            } else {
                aux = aux.getDer();
            }
        }
        if (aux == null) {
            return false;
        }
        if (aux.getIzq() == null && aux.getDer() == null) {
            if (aux == raiz) {
                raiz = null;
            } else if (padre.getIzq() == aux) {
                padre.setIzq(null);
            } else {
                padre.setDer(null);
            }
        } else if (aux.getIzq() == null) {
            if (aux == raiz) {
                raiz = aux.getDer();
            } else if (padre.getIzq() == aux) {
                padre.setIzq(aux.getDer());
            } else {
                padre.setDer(aux.getDer());
            }
        } else if (aux.getDer() == null) {
            if (aux == raiz) {
                raiz = aux.getIzq();
            } else if (padre.getIzq() == aux) {
                padre.setIzq(aux.getIzq());
            } else {
                padre.setDer(aux.getIzq());
            }
        } else {
            Nodo sucesor = getSucesor(aux);
            if (aux == raiz) {
                raiz = sucesor;
            } else if (padre.getIzq() == aux) {
                padre.setIzq(sucesor);
            } else {
                padre.setDer(sucesor);
            }
            sucesor.setIzq(aux.getIzq());
        }
        return true;
    }

    private Nodo getSucesor(Nodo nodo) {
        Nodo sucesor = nodo;
        Nodo sucesorPadre = nodo;
        Nodo actual = nodo.getDer();
        while (actual != null) {
            sucesorPadre = sucesor;
            sucesor = actual;
            actual = actual.getIzq();
        }
        if (sucesor != nodo.getDer()) {
            sucesorPadre.setIzq(sucesor.getDer());
            sucesor.setDer(nodo.getDer());
        }
        return sucesor;
    }
}
