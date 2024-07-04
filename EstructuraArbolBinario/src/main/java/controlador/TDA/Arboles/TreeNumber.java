/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.TDA.Arboles;

import controlador.TDA.listas.LinkedList;
import controlador.TDA.listas.exception.VacioException;
import java.util.Arrays;

/**
 *
 * @author sakotaz
 */
public class TreeNumber {
    private NodoTree <Integer> root;
    private Integer height;
    private LinkedList <LinkedList <NodoTree>> levels;
    private Integer nro_nodos;
    private LinkedList <NodoTree> orders;

    public TreeNumber() {
        root = null;
        height = 0;
        nro_nodos = 0;
        levels = new LinkedList<>();
        orders = new LinkedList<>();
    }
    
    
    
    private Integer calcHeight(NodoTree tree) throws Exception{
        if (tree == null) {
            return 0;
        } else {
            Integer left = calcHeight(tree.getLeft());
            Integer right = calcHeight(tree.getRigth());
            if (left.intValue() > right.intValue()){
                return left+1;
            } else {
                return right +1 ;
            }
        }          
    }
    
    private void calcLevels(NodoTree tree, Integer level) throws Exception{
        if (tree != null){
            getLevels().get(level).add(tree);
            level++;
            calcLevels(tree.getLeft(), level);
            calcLevels(tree.getRigth(), level);     
        } else if (level.intValue() != getHeight().intValue()) {
            levels.get(level).add(null);
            level++;
            calcLevels(null, level);
            calcLevels(null, level);
        }
    }
    
    private void levels () throws Exception{
        levels = new LinkedList<>();
        this.height = calcHeight(root);
        for (int i = 0; i <= this.height; i++) {
            levels.add(new LinkedList<>());
        }
        calcLevels(root, 0);
        levels.delete(levels.getSize() - 1);
    }
    
    public Boolean insert (Integer value) throws Exception {
        if (root == null) {
            root = new NodoTree<>(value);
            nro_nodos++;
            levels();
            return true;
        } else {
            NodoTree <Integer> fresh = new NodoTree<>(value);
            NodoTree <Integer> recent = root; 
            NodoTree <Integer> father;
            while (true){
                father = recent;
                if (value.intValue() == recent.getInfo().intValue()){
                    return false;
                } else if (value.intValue() < recent.getInfo().intValue()) {
                    recent = recent.getLeft();
                    if (recent == null){
                        fresh.setFather(father);
                        father.setLeft(fresh);
                        nro_nodos++;
                        levels();
                        return true;
                    }
                } else {
                    recent = recent.getRigth();
                    if (recent == null){
                        fresh.setFather(father);
                        father.setRigth(fresh);
                        nro_nodos++;
                        levels();
                        return true;
                    }    
                }
            }
        }
    }
    
    private void preOrden(NodoTree tree){
        if (tree != null) {
            orders.add(tree);
            preOrden(tree.getLeft());
            preOrden(tree.getRigth());
        }
    }
    
    private void postOrden(NodoTree tree){
        if (tree != null) {
            postOrden(tree.getLeft());
            postOrden(tree.getRigth());
            orders.add(tree);
        }
    }
    
    private void InOrden(NodoTree tree){
        if (tree != null) {
            InOrden(tree.getLeft());
            orders.add(tree);
            InOrden(tree.getRigth());
        }
    }
    
    
    
    public LinkedList <NodoTree> routes(Integer type) {
        //1 preorder
        //2 postorder
        //any in order
        orders = new LinkedList<>();
        if (type == 1)
            preOrden(root);
        else if (type == 2) 
            postOrden(root);
        else 
            InOrden(root);
        return orders;
    }
    
    public NodoTree <Integer> getRoot() {
        return root;
    }

    public void setRoot(NodoTree <Integer> root) {
        this.root = root;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public LinkedList <LinkedList <NodoTree>> getLevels() {
        return levels;
    }

    public void setLevels(LinkedList <LinkedList <NodoTree>> levels) {
        this.levels = levels;
    }

    public Integer getNro_nodos() {
        return nro_nodos;
    }

    public void setNro_nodos(Integer nro_nodos) {
        this.nro_nodos = nro_nodos;
    }

    public LinkedList <NodoTree> getOrders() {
        return orders;
    }

    public void setOrders(LinkedList <NodoTree> orders) {
        this.orders = orders;
    }
    
    public String printf1(String cadena) {
        cadena =  "   " + root + "       ";
        cadena = printf1(root, 1, cadena);
        return cadena;
     }
     
    private String printf1(NodoTree arbol, Integer valor, String cadena){       
        if (arbol.getLeft() != null) {
            if (valor == 2){
                cadena = cadena + arbol.getLeft().toString() +  "-" + arbol.getRigth().toString() + "-";
                return cadena;
            }
            cadena = cadena + "\n" + arbol.getLeft().toString() +  "---" + arbol.getRigth().toString() + "\n";
            cadena = printf1(arbol.getLeft(), valor+1, cadena);
            cadena = printf1(arbol.getRigth(), valor+1, cadena);

        }
        return cadena;
    }
     
    private void printf() {
        System.out.println(root.toString());
        printf(root);
    }
       
    private void printf(NodoTree arbol){       
        if (arbol.getLeft() != null) {
            System.out.println("|__ " + arbol.getLeft().toString() + "|__ " + arbol.getRigth().toString());
            printf(arbol.getLeft());
            printf(arbol.getRigth());
        }
    }
    
    public static void main(String[] args) {
        try {
            TreeNumber tree = new TreeNumber();
            tree.insert(90);
            tree.insert(50);
            tree.insert(100);
            tree.insert(5);
            tree.insert(60);
            tree.insert(95);
            tree.insert(110);
            System.out.println("ALTURA " + tree.getHeight());
            System.out.println("LVELS " + tree.getLevels().getSize());
            System.out.println(tree.printf1(""));
            LinkedList <NodoTree> route = tree.routes(1);
            System.out.println(route.getSize());
            System.out.println("RECORRIDO EN PRE ORDEN");
            Arrays.stream(tree.routes(1).toArray()).forEach(data -> System.out.print(data + "-- "));
            System.out.println("");
            System.out.println("RECORRIDO EN POST ORDEN");
            Arrays.stream(tree.routes(2).toArray()).forEach(data -> System.out.print(data + "-- "));
            System.out.println("");
            System.out.println("RECORRIDO EN IN ORDEN");
            Arrays.stream(tree.routes(0).toArray()).forEach(data -> System.out.print(data + "-- "));
            System.out.println("ARBOL");
            System.out.println("");
            NodoTree [] array = tree.routes(1).toArray();
            int j = 1;
            for (int i = 1; i <= tree.getLevels().getSize()+1; i++) {
                while (j != i*2) {
                    j--;
                    System.out.print(array[j] + " -------- ");
                    j = j + 2;
                }
                System.out.println("");
            }
        
        } catch (Exception e) {
            System.out.println("ERROR" + e.getMessage());
        }
    }
}
