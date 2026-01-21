package gui;

import arbol.Arbol;
import arbol.Nodo;
import arbol.Par;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Grafo extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private Arbol arbol;
    private JButton botonGenerar;
    private Ventana contenedor;
    private mxGraph graph;
    private mxGraphComponent graphComponent;
    private List<Par> listaPares;
    private Object parent;

    public Grafo(Ventana ventana) {
        super.setLayout(new BorderLayout());
        this.listaPares = new ArrayList();
        this.contenedor = ventana;
        new ArrayList();
        this.graph = new mxGraph();
        this.parent = this.graph.getDefaultParent();
        this.graphComponent = new mxGraphComponent(this.graph);
        this.botonGenerar = new JButton("Generar Archivo");
        this.botonGenerar.addActionListener(this);
        this.botonGenerar.setEnabled(false);
        super.add(this.graphComponent, "Center");
        super.add(this.botonGenerar, "South");
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == this.botonGenerar) {
            this.contenedor.generarArchivo();
        }

    }

    public void actualizar() {
        this.graph.selectAll();
        this.graph.removeCells();
        this.listaPares.clear();
        this.graph.getModel().beginUpdate();

        try {
            Object origen = this.graph.insertVertex(this.parent, this.arbol.getRaiz().toString(), this.arbol.getRaiz(), 0.0D, 0.0D, 80.0D, 30.0D);
            Par parOrigen = new Par(this.arbol.getRaiz(), origen);
            this.listaPares.add(parOrigen);
            this.addAristas(parOrigen, this.arbol.getRaiz().getHijos(), 0, 0);
        } finally {
            this.graph.getModel().endUpdate();
        }

    }

    private void addAristas(Par origen, List<Nodo> hijos, int i, int j) {
        Iterator<Nodo> it = hijos.iterator();

        Nodo n;
        int longitud;
        for (int x = i; it.hasNext(); x += longitud + n.getNumHijos() * 100 + 40) {
            n = (Nodo) it.next();
            if (this.contiene(n)) {
                Par p = this.getPar(n);
                this.graph.insertEdge(this.parent, (String) null, "", origen.getVertice(), p.getVertice());
                longitud = p.getLongitud();
            } else {
                longitud = n.toString().length() * 8;
                Object destino = this.graph.insertVertex(this.parent, n.toString(), n, (double) x, (double) (j + 100), (double) longitud, 30.0D);
                Par parDestino = new Par(n, destino);
                this.listaPares.add(parDestino);
                this.addAristas(parDestino, n.getHijos(), x, j + 100);
                this.graph.insertEdge(this.parent, (String) null, "", origen.getVertice(), destino);
            }
        }

    }

    private boolean contiene(Nodo n) {
        Iterator<Par> it = this.listaPares.iterator();
        boolean enc = false;

        while (it.hasNext() && !enc) {
            Par p = (Par) it.next();
            if (n.getTitulo().equals(p.getNodo().getTitulo())) {
                enc = true;
            }
        }

        return enc;
    }

    public void crearArbol(Arbol arbol) {
        this.arbol = arbol;
        this.actualizar();
        this.botonGenerar.setEnabled(true);
    }

    private Par getPar(Nodo n) {
        Iterator<Par> it = this.listaPares.iterator();
        boolean enc = false;
        Par p = null;

        while (it.hasNext() && !enc) {
            p = (Par) it.next();
            if (n.getTitulo().equals(p.getNodo().getTitulo())) {
                enc = true;
            }
        }

        return p;
    }
}
