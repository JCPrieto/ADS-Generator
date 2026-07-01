package gui;

import arbol.Arbol;
import arbol.Nodo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Editor extends JPanel implements ActionListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private Arbol arbol;
    private final JButton botonCancelar;
    private final JButton botonGuardar;
    private final Ventana contenedor;
    private final List<arbol.Atributo> listaAtributos;
    private final List<arbol.Campo> listaCampos;
    private final JComboBox<Nodo> listaNodos;
    private final List<arbol.Siguiente> listaSiguientes;
    private final List<arbol.Validacion> listaValidaciones;
    private JPanel panelAtributos;
    private JPanel panelCampos;
    private final JPanel panelElementos;
    private JPanel panelSiguiente;
    private JPanel panelValidacion;
    private final JScrollPane scrollElementos;

    public Editor(Ventana ventana) {
        this.contenedor = ventana;
        this.listaAtributos = new ArrayList<>();
        this.listaCampos = new ArrayList<>();
        this.listaValidaciones = new ArrayList<>();
        this.listaSiguientes = new ArrayList<>();
        super.setLayout(new BorderLayout());
        this.listaNodos = new JComboBox<>();
        this.listaNodos.addActionListener(this);
        this.panelElementos = new JPanel();
        this.inicializaPanelElementos();
        this.scrollElementos = new JScrollPane();
        this.scrollElementos.setViewportView(this.panelElementos);
        JPanel botonera = new JPanel();
        botonera.setLayout(new BorderLayout());
        this.botonGuardar = new JButton("Guardar");
        this.botonGuardar.addActionListener(this);
        this.botonGuardar.setEnabled(false);
        this.botonCancelar = new JButton("Cancelar");
        this.botonCancelar.addActionListener(this);
        this.botonCancelar.setEnabled(false);
        botonera.add(this.botonGuardar, "Center");
        botonera.add(this.botonCancelar, "West");
        super.add(this.listaNodos, "North");
        super.add(this.scrollElementos, "Center");
        super.add(botonera, "South");
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == this.listaNodos) {
            this.limpiarVentana();
            if (this.listaNodos.getSelectedIndex() != -1) {
                this.cargarNodo((Nodo) this.listaNodos.getSelectedItem());
                this.botonGuardar.setEnabled(true);
                this.botonCancelar.setEnabled(true);
                this.revalidate();
                this.repaint();
            } else {
                this.limpiarVentana();
                this.botonGuardar.setEnabled(false);
                this.botonCancelar.setEnabled(false);
            }
        }

        if (arg0.getSource() == this.botonGuardar) {
            Nodo n = (Nodo) this.listaNodos.getSelectedItem();
            n.addAtributos(this.listaAtributos);
            n.addCampos(this.listaCampos);
            n.addValidaciones(this.listaValidaciones);
            n.addSiguiente(this.listaSiguientes);
            this.recargarListaNodos();
            this.limpiarVentana();
            this.contenedor.actualizarGrafo();
            this.revalidate();
            this.repaint();
        }

        if (arg0.getSource() == this.botonCancelar) {
            this.limpiarVentana();
            this.listaNodos.setSelectedIndex(-1);
            this.botonGuardar.setEnabled(false);
            this.botonCancelar.setEnabled(false);
        }

    }

    public void actualizarAtributo(Atributo atributo) {
        arbol.Atributo a = new arbol.Atributo(atributo.getNombre(), atributo.getValor(), atributo.getDescripcion());
        this.listaAtributos.remove(a);
        Nodo n = (Nodo) this.listaNodos.getSelectedItem();
        n.removeAtributo(a);
    }

    public void actualizarCampo(Campo campo) {
        arbol.Campo c = new arbol.Campo(campo.getNombre(), campo.getTipo(), campo.getEtiqueta(), campo.getValor(), campo.getEnlace());
        this.listaCampos.remove(c);
        Nodo n = (Nodo) this.listaNodos.getSelectedItem();
        n.removeCampo(c);
    }

    public void actualizarSiguiente(Siguiente siguiente) {
        arbol.Siguiente s = new arbol.Siguiente(siguiente.getCondicion(), siguiente.getDestino());
        this.listaSiguientes.remove(s);
        Nodo n = (Nodo) this.listaNodos.getSelectedItem();
        n.removeHijo(siguiente.getDestino());
        n.removeSiguiente(s);
    }

    public void actualizarValidacion(Validacion validacion) {
        arbol.Validacion v = new arbol.Validacion(validacion.getCondicion(), validacion.getMensaje());
        this.listaValidaciones.remove(v);
        Nodo n = (Nodo) this.listaNodos.getSelectedItem();
        n.removeValidacion(v);
    }

    public void addAtributo(Atributo atributo) {
        arbol.Atributo atr = new arbol.Atributo(atributo.getNombre(), atributo.getValor(), atributo.getDescripcion());
        if (this.listaAtributos.contains(atr)) {
            Advertencia a = new Advertencia("<html><b>Atributo ya especificado.<br>Especifique otro nombre para el atributo.</b></html>");
            a.setVisible(true);
            atributo.cancelaAdd();
        } else {
            if (!atributo.modificado()) {
                GridBagLayout g = (GridBagLayout) this.panelAtributos.getLayout();
                GridBagConstraints c = g.getConstraints(this.panelAtributos);
                c.fill = 1;
                c.insets = new Insets(5, 5, 5, 5);
                Atributo a = new Atributo(this);
                c.gridx = 0;
                c.gridy = -1;
                c.weightx = 1.0D;
                c.weighty = 1.0D;
                g.setConstraints(a, c);
                this.panelAtributos.add(a);
                this.repintarElementos();
            }

            this.listaAtributos.add(atr);
        }

    }

    public void addCampo(Campo campo) {
        arbol.Campo camp = new arbol.Campo(campo.getNombre(), campo.getTipo(), campo.getEtiqueta(), campo.getValor(), campo.getEnlace());
        if (this.listaCampos.contains(camp)) {
            Advertencia a = new Advertencia("<html><b>Campo ya especificado.<br>Especifique otro nombre para el campo.</b></html>");
            a.setVisible(true);
            campo.cancelaAdd();
        } else {
            if (!campo.modificado()) {
                GridBagLayout g = (GridBagLayout) this.panelCampos.getLayout();
                GridBagConstraints c = g.getConstraints(this.panelCampos);
                c.fill = 1;
                c.insets = new Insets(5, 5, 5, 5);
                Campo cmp = new Campo(this);
                c.gridx = 0;
                c.gridy = -1;
                c.weightx = 1.0D;
                c.weighty = 1.0D;
                g.setConstraints(cmp, c);
                this.panelCampos.add(cmp);
                this.repintarElementos();
            }

            this.listaCampos.add(camp);
        }

    }

    public void addSiguiente(Siguiente siguiente) {
        arbol.Siguiente sig = new arbol.Siguiente(siguiente.getCondicion(), siguiente.getDestino());
        Advertencia a;
        if (this.listaSiguientes.contains(sig)) {
            a = new Advertencia("<html><b>La condici�n ya esta especificada.<br>Indique otra condicion para ir al siguiente nodo.</b></html>");
            a.setVisible(true);
            siguiente.cancelaAdd();
        } else if (((Nodo) this.listaNodos.getSelectedItem()).getTitulo().equals(sig.getDestino())) {
            a = new Advertencia("<html><b>Un nodo no debe conducir a si mismo</b></html>");
            a.setVisible(true);
            siguiente.cancelaAdd();
        } else {
            if (!siguiente.modificado()) {
                GridBagLayout g = (GridBagLayout) this.panelSiguiente.getLayout();
                GridBagConstraints c = g.getConstraints(this.panelSiguiente);
                c.fill = 1;
                c.insets = new Insets(5, 5, 5, 5);
                Siguiente s = new Siguiente(this);
                c.gridx = 0;
                c.gridy = -1;
                c.weightx = 1.0D;
                c.weighty = 1.0D;
                g.setConstraints(s, c);
                this.panelSiguiente.add(s);
                this.repintarElementos();
            }

            this.listaSiguientes.add(sig);
        }

    }

    public void addValidacion(Validacion validacion) {
        arbol.Validacion val = new arbol.Validacion(validacion.getCondicion(), validacion.getMensaje());
        if (this.listaValidaciones.contains(val)) {
            Advertencia a = new Advertencia("<html><b>Condicion de validacion ya especificada.<br>Indique otra condicion para la validacion del nodo.</b></html>");
            a.setVisible(true);
            validacion.cancelaAdd();
        } else {
            if (!validacion.modificado()) {
                GridBagLayout g = (GridBagLayout) this.panelValidacion.getLayout();
                GridBagConstraints c = g.getConstraints(this.panelValidacion);
                c.fill = 1;
                c.insets = new Insets(5, 5, 5, 5);
                Validacion v = new Validacion(this);
                c.gridx = 0;
                c.gridy = -1;
                c.weightx = 1.0D;
                c.weighty = 1.0D;
                g.setConstraints(v, c);
                this.panelValidacion.add(v);
                this.repintarElementos();
            }

            this.listaValidaciones.add(val);
        }

    }

    private void cargarAtributos(Nodo selectedItem) {
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.panelAtributos.setLayout(g);
        c.fill = 1;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1.0D;
        c.weighty = 1.0D;
        int i = 0;

        for (Iterator<arbol.Atributo> it = selectedItem.getAtributos(); it.hasNext(); ++i) {
            arbol.Atributo a = it.next();
            Atributo pa = new Atributo(a, this);
            c.gridx = 0;
            c.gridy = i;
            g.setConstraints(pa, c);
            this.panelAtributos.add(pa);
        }

        Atributo pa = new Atributo(this);
        c.gridx = 0;
        c.gridy = i;
        g.setConstraints(pa, c);
        this.panelAtributos.add(pa);
        this.repintarElementos();
    }

    private void cargarCampos(Nodo selectedItem) {
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.panelCampos.setLayout(g);
        c.fill = 1;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1.0D;
        c.weighty = 1.0D;
        int i = 0;

        for (Iterator<arbol.Campo> it = selectedItem.getCampos(); it.hasNext(); ++i) {
            arbol.Campo cmp = it.next();
            Campo pc = new Campo(cmp, this);
            c.gridx = 0;
            c.gridy = i;
            g.setConstraints(pc, c);
            this.panelCampos.add(pc);
        }

        Campo pc = new Campo(this);
        c.gridx = 0;
        c.gridy = i;
        g.setConstraints(pc, c);
        this.panelCampos.add(pc);
        this.repintarElementos();
    }

    private void cargarNodo(Nodo selectedItem) {
        this.cargarAtributos(selectedItem);
        this.cargarCampos(selectedItem);
        this.cargarValidacion(selectedItem);
        this.cargarSiguiente(selectedItem);
    }

    private void cargarSiguiente(Nodo selectedItem) {
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.panelSiguiente.setLayout(g);
        c.fill = 1;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1.0D;
        c.weighty = 1.0D;
        int i = 0;

        for (Iterator<arbol.Siguiente> it = selectedItem.getSiguientes(); it.hasNext(); ++i) {
            arbol.Siguiente s = it.next();
            Siguiente ps = new Siguiente(s, this);
            c.gridx = 0;
            c.gridy = i;
            g.setConstraints(ps, c);
            this.panelSiguiente.add(ps);
        }

        Siguiente ps = new Siguiente(this);
        c.gridx = 0;
        c.gridy = i;
        g.setConstraints(ps, c);
        this.panelSiguiente.add(ps);
        this.repintarElementos();
    }

    private void cargarValidacion(Nodo selectedItem) {
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.panelValidacion.setLayout(g);
        c.fill = 1;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1.0D;
        c.weighty = 1.0D;
        int i = 0;

        for (Iterator<arbol.Validacion> it = selectedItem.getValidaciones(); it.hasNext(); ++i) {
            arbol.Validacion v = it.next();
            Validacion pv = new Validacion(v, this);
            c.gridx = 0;
            c.gridy = i;
            g.setConstraints(pv, c);
            this.panelValidacion.add(pv);
        }

        Validacion pv = new Validacion(this);
        c.gridx = 0;
        c.gridy = i;
        g.setConstraints(pv, c);
        this.panelValidacion.add(pv);
        this.repintarElementos();
    }

    public void crearArbol(Arbol arbol) {
        this.limpiarVentana();
        this.listaNodos.removeAllItems();
        this.arbol = arbol;
        this.recargarListaNodos();
    }

    public List<String> getAtributos() {
        List<String> l = new ArrayList<>(this.arbol.getAtributos());

        for (arbol.Atributo a : this.listaAtributos) {
            l.add(a.getNombre());
        }

        Collections.sort(l);
        return l;
    }

    public List<String> getNodos() {
        List<String> l = new ArrayList<>();
        Iterator<Nodo> it = this.arbol.getNodos();

        while (it.hasNext()) {
            l.add(it.next().getTitulo());
        }

        return l;
    }

    private void inicializaPanelElementos() {
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.panelElementos.setLayout(g);
        c.fill = 1;
        c.insets = new Insets(10, 10, 10, 10);
        this.panelAtributos = new JPanel();
        this.panelAtributos.setBorder(BorderFactory.createTitledBorder("Atributos: "));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0D;
        c.weighty = 0.5D;
        g.setConstraints(this.panelAtributos, c);
        this.panelElementos.add(this.panelAtributos);
        this.panelCampos = new JPanel();
        this.panelCampos.setBorder(BorderFactory.createTitledBorder("Campos: "));
        c.gridx = 0;
        c.gridy = 1;
        g.setConstraints(this.panelCampos, c);
        this.panelElementos.add(this.panelCampos);
        this.panelValidacion = new JPanel();
        this.panelValidacion.setBorder(BorderFactory.createTitledBorder("Validacion: "));
        c.gridx = 0;
        c.gridy = 2;
        g.setConstraints(this.panelValidacion, c);
        this.panelElementos.add(this.panelValidacion);
        this.panelSiguiente = new JPanel();
        this.panelSiguiente.setBorder(BorderFactory.createTitledBorder("Siguiente: "));
        c.gridx = 0;
        c.gridy = 3;
        g.setConstraints(this.panelSiguiente, c);
        this.panelElementos.add(this.panelSiguiente);
    }

    private void limpiarVentana() {
        this.panelAtributos.removeAll();
        this.listaAtributos.clear();
        this.panelCampos.removeAll();
        this.listaCampos.clear();
        this.panelValidacion.removeAll();
        this.listaValidaciones.clear();
        this.panelSiguiente.removeAll();
        this.listaSiguientes.clear();
    }

    private void recargarListaNodos() {
        this.listaNodos.removeAllItems();
        Iterator<Nodo> it = this.arbol.getNodos();

        while (it.hasNext()) {
            Nodo n = it.next();
            this.listaNodos.addItem(n);
        }

        this.listaNodos.setSelectedIndex(-1);
    }

    public void removeAtributo(Atributo atributo) {
        this.panelAtributos.remove(atributo);
        this.repintarElementos();
        this.actualizarAtributo(atributo);
    }

    public void removeCampo(Campo campo) {
        this.panelCampos.remove(campo);
        this.repintarElementos();
        this.actualizarCampo(campo);
    }

    public void removeSiguiente(Siguiente siguiente) {
        this.actualizarSiguiente(siguiente);
        this.panelSiguiente.remove(siguiente);
        this.repintarElementos();
    }

    public void removeValidacion(Validacion validacion) {
        this.panelValidacion.remove(validacion);
        this.repintarElementos();
        this.actualizarValidacion(validacion);
    }

    public void repintarElementos() {
        this.scrollElementos.revalidate();
        this.scrollElementos.repaint();
    }
}
