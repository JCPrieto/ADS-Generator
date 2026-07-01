package data;

import arbol.*;
import gui.Ventana;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

public class Proyecto {
    private boolean saved;
    private Arbol arbol;
    private JDialog d;

    public Proyecto() {
        this.saved = true;
    }

    public Proyecto(String selectedValue) {
        this();
        this.crearArbol(selectedValue);
    }

    private void crearArbol(String selectedValue) {
        SAXBuilder b = new SAXBuilder();

        try {
            Document doc = b.build(new File(selectedValue + ".pds"));
            Element root = doc.getRootElement();
            this.arbol = new Arbol(this.reconversion(root.getName()));
            List<Element> nodos = root.getChildren();

            for (Element n : nodos) {
                Nodo nd = new Nodo(this.reconversion(n.getName()), this.arbol);
                Element atributos = n.getChild("Atributos");
                Iterator<Element> ita = atributos.getChildren().iterator();

                Element campos;
                while (ita.hasNext()) {
                    campos = ita.next();
                    Atributo atr = new Atributo(this.reconversion(campos.getName()), campos.getAttributeValue("Valor"), campos.getAttributeValue("Descripcion"));
                    nd.addAtributos(atr);
                    this.arbol.addAtributo(atr);
                }

                campos = n.getChild("Campos");
                Iterator<Element> itc = campos.getChildren().iterator();

                Element validaciones;
                while (itc.hasNext()) {
                    validaciones = itc.next();
                    Campo cmp = new Campo(this.reconversion(validaciones.getName()), validaciones.getAttributeValue("Tipo"), validaciones.getAttributeValue("Etiqueta"), validaciones.getAttributeValue("Valor"), validaciones.getAttributeValue("Enlace"));
                    nd.addCampo(cmp);
                }

                validaciones = n.getChild("Validaciones");
                Iterator<Element> itv = validaciones.getChildren().iterator();

                Element siguientes;
                while (itv.hasNext()) {
                    siguientes = itv.next();
                    Validacion val = new Validacion(siguientes.getAttributeValue("Condicion"), siguientes.getAttributeValue("Mensaje"));
                    nd.addValidacion(val);
                }

                siguientes = n.getChild("Siguientes");
                Iterator<Element> its = siguientes.getChildren().iterator();

                Element hijos;
                while (its.hasNext()) {
                    hijos = its.next();
                    Siguiente sgt = new Siguiente(hijos.getAttributeValue("Condicion"), hijos.getAttributeValue("Destino"));
                    nd.addSiguiente(sgt);
                }

                hijos = n.getChild("Hijos");

                for (Element h : hijos.getChildren()) {
                    Nodo hijo = new Nodo(this.reconversion(h.getName()), this.arbol);
                    if (this.arbol.contiene(hijo)) {
                        nd.addHijo(this.arbol.getNodo(this.reconversion(h.getName())));
                    } else {
                        nd.addHijo(hijo);
                    }
                }

                this.arbol.addNodo(nd);
            }
        } catch (JDOMException | IOException var21) {
            var21.printStackTrace();
        }

    }

    private String reconversion(String name) {
        String s;
        if (name.startsWith("_")) {
            s = name.substring(1);
        } else {
            s = name;
        }

        return s.replace("_", " ");
    }

    public boolean getSaved() {
        return this.saved;
    }

    public void mostrarSalvado(final Ventana ventana) {
        this.d = new JDialog(ventana);
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.insets = new Insets(5, 5, 5, 5);
        this.d.setLayout(g);
        JLabel l = new JLabel("�Desea salvar el proyecto antes de salir?");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.weightx = 1.0D;
        g.setConstraints(l, c);
        JButton guardar = new JButton("Guardar");
        guardar.addActionListener(e -> {
            Proyecto.this.cerrarDialogo();
            Proyecto.this.salvarProyecto();
            ventana.dispose();
        });
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0.0D;
        g.setConstraints(guardar, c);
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(e -> Proyecto.this.cerrarDialogo());
        c.gridx = 2;
        c.gridy = 1;
        g.setConstraints(cancelar, c);
        JButton omitir = new JButton("Cerrar de todas formas");
        omitir.addActionListener(e -> ventana.dispose());
        c.gridx = 3;
        c.gridy = 1;
        g.setConstraints(omitir, c);
        this.d.add(l);
        this.d.add(guardar);
        this.d.add(cancelar);
        this.d.add(omitir);
        this.d.pack();
        this.d.setVisible(true);
    }

    protected void salvarProyecto() {
        Document doc = new Document();
        Element root = new Element(this.conversion(this.arbol.getNombre()));
        doc.setRootElement(root);
        Iterator<Nodo> it = this.arbol.getNodos();

        while (it.hasNext()) {
            Nodo n = it.next();
            this.escribirNodo(root, n);
        }

        XMLOutputter out = new XMLOutputter();

        try {
            FileOutputStream file = new FileOutputStream(this.arbol.getNombre() + ".pds");
            out.output((Document) doc, (OutputStream) file);
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }

    private void escribirNodo(Element root, Nodo raiz) {
        Element nodo = new Element(this.conversion(raiz.getTitulo()));
        Element atributos = new Element("Atributos");
        Iterator<Atributo> ita = raiz.getAtributos();

        while (ita.hasNext()) {
            Atributo a = ita.next();
            Element atributo = new Element(this.conversion(a.getNombre()));
            atributo.setAttribute("Valor", a.getValor());
            atributo.setAttribute("Descripcion", a.getDescripcion());
            atributos.addContent(atributo);
        }

        nodo.addContent(atributos);
        Element campos = new Element("Campos");
        Iterator<Campo> itc = raiz.getCampos();

        while (itc.hasNext()) {
            Campo c = itc.next();
            Element campo = new Element(this.conversion(c.getNombre()));
            campo.setAttribute("Tipo", c.getTipo());
            campo.setAttribute("Etiqueta", c.getEtiqueta());
            campo.setAttribute("Valor", c.getValor());
            campo.setAttribute("Enlace", c.getEnlace());
            campos.addContent(campo);
        }

        nodo.addContent(campos);
        Element validaciones = new Element("Validaciones");
        Iterator<Validacion> itv = raiz.getValidaciones();

        int i;
        for (i = 1; itv.hasNext(); ++i) {
            Validacion v = itv.next();
            Element validacion = new Element("Validacion_" + i);
            validacion.setAttribute("Condicion", v.getCondicion());
            validacion.setAttribute("Mensaje", v.getMensaje());
            validaciones.addContent(validacion);
        }

        nodo.addContent(validaciones);
        Element siguientes = new Element("Siguientes");
        Iterator<Siguiente> its = raiz.getSiguientes();

        for (i = 1; its.hasNext(); ++i) {
            Siguiente s = its.next();
            Element siguiente = new Element("Siguiente_" + i);
            siguiente.setAttribute("Condicion", s.getCondicion());
            siguiente.setAttribute("Destino", s.getDestino());
            siguientes.addContent(siguiente);
        }

        nodo.addContent(siguientes);
        Element hijos = new Element("Hijos");

        for (Nodo h : raiz.getHijos()) {
            hijos.addContent(new Element(this.conversion(h.getTitulo())));
        }

        nodo.addContent(hijos);
        root.addContent(nodo);
    }

    private String conversion(String titulo) {
        String s;
        if (!titulo.startsWith("0") && !titulo.startsWith("1") && !titulo.startsWith("2") && !titulo.startsWith("3") && !titulo.startsWith("4") && !titulo.startsWith("5") && !titulo.startsWith("6") && !titulo.startsWith("7") && !titulo.startsWith("8") && !titulo.startsWith("9")) {
            s = titulo;
        } else {
            s = "_" + titulo;
        }

        return s.replace(" ", "_");
    }

    protected void cerrarDialogo() {
        this.d.dispose();
    }

    public Arbol getArbol() {
        return this.arbol;
    }

    public void setArbol(Arbol arbol) {
        this.arbol = arbol;
        this.saved = false;
    }

    public void SetSaved(boolean saved) {
        this.saved = saved;
    }

    public void guardar() {
        this.salvarProyecto();
        this.saved = true;
    }

    public boolean vacio() {
        return this.arbol == null;
    }
}
