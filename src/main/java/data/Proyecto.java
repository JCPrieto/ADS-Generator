package data;

import arbol.*;
import gui.Ventana;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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
            Iterator it = nodos.iterator();

            while (it.hasNext()) {
                Element n = (Element) it.next();
                Nodo nd = new Nodo(this.reconversion(n.getName()), this.arbol);
                Element atributos = n.getChild("Atributos");
                Iterator ita = atributos.getChildren().iterator();

                Element campos;
                while (ita.hasNext()) {
                    campos = (Element) ita.next();
                    Atributo atr = new Atributo(this.reconversion(campos.getName()), campos.getAttributeValue("Valor"), campos.getAttributeValue("Descripcion"));
                    nd.addAtributos(atr);
                    this.arbol.addAtributo(atr);
                }

                campos = n.getChild("Campos");
                Iterator itc = campos.getChildren().iterator();

                Element validaciones;
                while (itc.hasNext()) {
                    validaciones = (Element) itc.next();
                    Campo cmp = new Campo(this.reconversion(validaciones.getName()), validaciones.getAttributeValue("Tipo"), validaciones.getAttributeValue("Etiqueta"), validaciones.getAttributeValue("Valor"), validaciones.getAttributeValue("Enlace"));
                    nd.addCampo(cmp);
                }

                validaciones = n.getChild("Validaciones");
                Iterator itv = validaciones.getChildren().iterator();

                Element siguientes;
                while (itv.hasNext()) {
                    siguientes = (Element) itv.next();
                    Validacion val = new Validacion(siguientes.getAttributeValue("Condicion"), siguientes.getAttributeValue("Mensaje"));
                    nd.addValidacion(val);
                }

                siguientes = n.getChild("Siguientes");
                Iterator its = siguientes.getChildren().iterator();

                Element hijos;
                while (its.hasNext()) {
                    hijos = (Element) its.next();
                    Siguiente sgt = new Siguiente(hijos.getAttributeValue("Condicion"), hijos.getAttributeValue("Destino"));
                    nd.addSiguiente(sgt);
                }

                hijos = n.getChild("Hijos");
                Iterator ith = hijos.getChildren().iterator();

                while (ith.hasNext()) {
                    Element h = (Element) ith.next();
                    Nodo hijo = new Nodo(this.reconversion(h.getName()), this.arbol);
                    if (this.arbol.contiene(hijo)) {
                        nd.addHijo(this.arbol.getNodo(this.reconversion(h.getName())));
                    } else {
                        nd.addHijo(hijo);
                    }
                }

                this.arbol.addNodo(nd);
            }
        } catch (JDOMException var21) {
            var21.printStackTrace();
        } catch (IOException var22) {
            var22.printStackTrace();
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
        guardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Proyecto.this.cerrarDialogo();
                Proyecto.this.salvarProyecto();
                ventana.dispose();
            }
        });
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0.0D;
        g.setConstraints(guardar, c);
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Proyecto.this.cerrarDialogo();
            }
        });
        c.gridx = 2;
        c.gridy = 1;
        g.setConstraints(cancelar, c);
        JButton omitir = new JButton("Cerrar de todas formas");
        omitir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventana.dispose();
            }
        });
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
        Iterator it = this.arbol.getNodos();

        while (it.hasNext()) {
            Nodo n = (Nodo) it.next();
            this.escribirNodo(root, n);
        }

        XMLOutputter out = new XMLOutputter();

        try {
            FileOutputStream file = new FileOutputStream(this.arbol.getNombre() + ".pds");
            out.output((Document) doc, (OutputStream) file);
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }

    private void escribirNodo(Element root, Nodo raiz) {
        Element nodo = new Element(this.conversion(raiz.getTitulo()));
        Element atributos = new Element("Atributos");
        Iterator ita = raiz.getAtributos();

        while (ita.hasNext()) {
            Atributo a = (Atributo) ita.next();
            Element atributo = new Element(this.conversion(a.getNombre()));
            atributo.setAttribute("Valor", a.getValor());
            atributo.setAttribute("Descripcion", a.getDescripcion());
            atributos.addContent((Content) atributo);
        }

        nodo.addContent((Content) atributos);
        Element campos = new Element("Campos");
        Iterator itc = raiz.getCampos();

        while (itc.hasNext()) {
            Campo c = (Campo) itc.next();
            Element campo = new Element(this.conversion(c.getNombre()));
            campo.setAttribute("Tipo", c.getTipo());
            campo.setAttribute("Etiqueta", c.getEtiqueta());
            campo.setAttribute("Valor", c.getValor());
            campo.setAttribute("Enlace", c.getEnlace());
            campos.addContent((Content) campo);
        }

        nodo.addContent((Content) campos);
        Element validaciones = new Element("Validaciones");
        Iterator<Validacion> itv = raiz.getValidaciones();

        int i;
        for (i = 1; itv.hasNext(); ++i) {
            Validacion v = (Validacion) itv.next();
            Element validacion = new Element("Validacion_" + i);
            validacion.setAttribute("Condicion", v.getCondicion());
            validacion.setAttribute("Mensaje", v.getMensaje());
            validaciones.addContent((Content) validacion);
        }

        nodo.addContent((Content) validaciones);
        Element siguientes = new Element("Siguientes");
        Iterator<Siguiente> its = raiz.getSiguientes();

        for (i = 1; its.hasNext(); ++i) {
            Siguiente s = (Siguiente) its.next();
            Element siguiente = new Element("Siguiente_" + i);
            siguiente.setAttribute("Condicion", s.getCondicion());
            siguiente.setAttribute("Destino", s.getDestino());
            siguientes.addContent((Content) siguiente);
        }

        nodo.addContent((Content) siguientes);
        Element hijos = new Element("Hijos");
        Iterator ith = raiz.getHijos().iterator();

        while (ith.hasNext()) {
            Nodo h = (Nodo) ith.next();
            hijos.addContent((Content) (new Element(this.conversion(h.getTitulo()))));
        }

        nodo.addContent((Content) hijos);
        root.addContent((Content) nodo);
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

    public void SetSaved(boolean b) {
        this.saved = false;
    }

    public void guardar() {
        this.salvarProyecto();
        this.saved = true;
    }

    public boolean vacio() {
        return this.arbol == null;
    }
}
