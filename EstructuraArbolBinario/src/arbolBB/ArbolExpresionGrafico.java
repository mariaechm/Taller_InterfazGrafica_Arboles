package arbolBB;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import javax.swing.*;

public class ArbolExpresionGrafico extends JPanel 
{
    private ArbolBB miArbol;
    private HashMap<Nodo, Ellipse2D> posicionNodos = null;
    private HashMap<Nodo, Dimension> subtreeSizes = null;
    private boolean dirty = true;
    private int parent2child = 20, child2child = 30;
    private Dimension empty = new Dimension(0,0);
    private FontMetrics fm = null;
    private int orden = 1;

    /**
     * Constructor de la clase ArbolExpresionGrafico.
     * El constructor permite inicializar los atributos de la clase ArbolExpresionGrafico
     * y llama al método repaint(), que es el encargado de pintar el Arbol.
     * @param miArbol: dato de tipo ArbolBB que contiene el Arbol a dibujar.
     */
    public ArbolExpresionGrafico(ArbolBB miArbol) 
    {
        this.miArbol = miArbol;
        this.setBackground(Color.WHITE);
        posicionNodos = new HashMap<>();
        subtreeSizes = new HashMap<>();
        dirty = true;
        repaint();      
    }

    /**
     * Calcula las posiciones de los respectivos subárboles y de cada nodo que 
     * forma parte de ese subárbol, para conocer en que posición van a ir dibujados
     * los círculos representativos del árbol de la expresión.
     */
    private void calcularPosiciones() 
    {
        posicionNodos.clear();
        subtreeSizes.clear();
        Nodo root = this.miArbol.getRaiz();
        if (root!= null) 
        {
            calcularTamañoSubarbol(root);
            calcularPosicion(root, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        }
    }
    
    /**
     * Calcula el tamaño de cada subárbol y lo agrega al objeto subtreeSizes de la clase
     * de tipo HashMap que va a contener la coleccion de todos los 
     * subárboles que contiene un arbol.
     * @param n: Objeto de la clase Nodo que se utiliza como
     * referencia calcular el tamaño de cada subárbol.
     * @return Dimension con el tamaño de cada subárbol.
     */
    private Dimension calcularTamañoSubarbol(Nodo n) 
    {
        if (n == null) 
            return new Dimension(0,0);

        Dimension ld = calcularTamañoSubarbol(n.getIzq());
        Dimension rd = calcularTamañoSubarbol(n.getDer());
          
        int h = fm.getHeight() + parent2child + Math.max(ld.height, rd.height);
        int w = ld.width + child2child + rd.width;
          
        Dimension d = new Dimension(w, h);
        subtreeSizes.put(n, d);
          
        return d;
    }
    
    /**
     * Calcula la ubicación de cada nodo de cada subárbol y agrega cada nodo con 
     * un objeto de tipo Ellipse2D que tiene la ubicación y la información específica de dónde 
     * va a ser dibujado.
     * @param n: Objeto de tipo Nodo que se utiliza como
     * referencia para calcular la ubicación de cada nodo.
     * @param left: int con alineación y orientación a la izquierda.
     * @param right: int con alineación y orientación a la derecha.
     * @param top: int con el tope.
     */
    private void calcularPosicion(Nodo n, int left, int right, int top) 
    {
        if (n == null) 
            return;

        Dimension ld = subtreeSizes.getOrDefault(n.getIzq(), empty);
        Dimension rd = subtreeSizes.getOrDefault(n.getDer(), empty);

        int center = 0;

        if (right!= Integer.MAX_VALUE)
            center = right - rd.width - child2child / 2;
        else if (left!= Integer.MAX_VALUE)
            center = left + ld.width + child2child / 2;

        int width = fm.stringWidth(n.getDato()+"");

        // Ajustar el tamaño del círculo para que no sobrepase los vértices
        int circleRadius = Math.max(width, fm.getHeight()) / 2 + 2; // Reducir el +10 a +2

        Ellipse2D circle = new Ellipse2D.Double(center - circleRadius, top, 2 * circleRadius, 2 * circleRadius);

        posicionNodos.put(n, circle);

        calcularPosicion(n.getIzq(), Integer.MAX_VALUE, center - child2child / 2, top + 2 * circleRadius + parent2child);
        calcularPosicion(n.getDer(), center + child2child / 2, Integer.MAX_VALUE, top + 2 * circleRadius + parent2child);
    }

    /**
     * Método para dibujar el árbol en el panel.
     * @param g: Objeto de la clase Graphics2D.
     * @param n: Nodo actual del árbol.
     * @param puntox: Coordenada x del nodo padre.
     * @param puntoy: Coordenada y del nodo padre.
     * @param yoffs: Desplazamiento vertical.
     */
    private void dibujarArbol(Graphics2D g, Nodo n, int puntox, int puntoy, int yoffs) 
    {
        if (n == null) 
            return;

        Ellipse2D circle = posicionNodos.get(n);

        // Dibujar el borde del círculo
        g.setColor(Color.BLACK);
        g.draw(circle);

        // Rellenar el círculo con color
        g.setColor(Color.CYAN); // Cambia el color según tu preferencia
        g.fill(circle);

        // Dibujar el texto del nodo
        g.setColor(Color.BLACK);
        g.drawString(n.getDato()+"", 
            (int)(circle.getX() + circle.getWidth() / 2 - fm.stringWidth(n.getDato()+"") / 2), 
            (int)(circle.getY() + circle.getHeight() / 2 + fm.getHeight() / 4));

        // Dibujar el orden del nodo
        g.drawString(String.valueOf(orden++), 
            (int)(circle.getX() + circle.getWidth() / 2 - fm.stringWidth(String.valueOf(orden-1)) / 2), 
            (int)(circle.getY() - fm.getHeight() / 2));

        int centerX = (int) (circle.getX() + circle.getWidth() / 2);
        int centerY = (int) (circle.getY() + circle.getHeight() / 2);
        int radius = (int) (circle.getWidth() / 2);

        if (puntox!= Integer.MAX_VALUE) {
            double angle = Math.atan2(centerY - puntoy, centerX - puntox);
            int startX = (int) (centerX - radius * Math.cos(angle));
            int startY = (int) (centerY - radius * Math.sin(angle));
            g.drawLine(puntox, puntoy, startX, startY);
        }

        dibujarArbol(g, n.getIzq(), centerX, centerY + radius, yoffs);
        dibujarArbol(g, n.getDer(), centerX, centerY + radius, yoffs);
    }

    /**
     * Sobreescribe el método paint y se encarga de pintar todo el árbol.
     * @param g: Objeto de la clase Graphics.
     */
    @Override
    public void paint(Graphics g) 
    {
        super.paint(g);
        fm = g.getFontMetrics();

        if (dirty) 
        {
            calcularPosiciones();
            dirty = false;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getWidth() / 2, parent2child);
        orden = 1; // Resetear el orden antes de dibujar el árbol
        dibujarArbol(g2d, this.miArbol.getRaiz(), Integer.MAX_VALUE, Integer.MAX_VALUE, fm.getLeading() + fm.getAscent());
        fm = null;
    }
    
    /**
     * Método para eliminar un nodo del árbol y actualizar el gráfico.
     * @param dato: valor del nodo a eliminar.
     */
    public void eliminarNodo(int dato) {
        miArbol.eliminar(dato);
        dirty = true;
        repaint();
    }
}