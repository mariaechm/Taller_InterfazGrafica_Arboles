/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.TDA.Arboles;

/**
 *
 * @author jossibel Perez
 */
public class NodoTree <E> {
    private NodoTree father;
    private NodoTree left;
    private NodoTree rigth;
    private E info;

    public NodoTree(E dato) {
        this.info = dato;
        father = null;
        left = null;
        rigth = null;
    }

    
    
    public NodoTree getFather() {
        return father;
    }

    public void setFather(NodoTree father) {
        this.father = father;
    }

    public NodoTree getLeft() {
        return left;
    }

    public void setLeft(NodoTree left) {
        this.left = left;
    }

    public NodoTree getRigth() {
        return rigth;
    }

    public void setRigth(NodoTree rigth) {
        this.rigth = rigth;
    }

    public E getInfo() {
        return info;
    }

    public void setInfo(E info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return info.toString();
    }
    
}
