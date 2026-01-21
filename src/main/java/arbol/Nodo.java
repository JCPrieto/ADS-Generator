package arbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Nodo {
    private String titulo;
    private List<Nodo> hijos;
    private List<Campo> campos;
    private List<Atributo> atributos;
    private Arbol arbol;
    private List<Validacion> validaciones;
    private List<Siguiente> siguientes;

    public Nodo(String string, Arbol arbol) {
        this.titulo = string.replace(" ", "_");
        this.hijos = new ArrayList();
        this.campos = new ArrayList();
        this.atributos = new ArrayList();
        this.validaciones = new ArrayList();
        this.siguientes = new ArrayList();
        this.arbol = arbol;
    }

    public String toString() {
        return this.titulo;
    }

    public Iterator<Atributo> getAtributos() {
        return this.atributos.iterator();
    }

    public Iterator<Campo> getCampos() {
        return this.campos.iterator();
    }

    public Iterator<Validacion> getValidaciones() {
        return this.validaciones.iterator();
    }

    public Iterator<Siguiente> getSiguientes() {
        return this.siguientes.iterator();
    }

    public void addAtributos(List<Atributo> listaAtributos) {
        this.atributos.addAll(listaAtributos);
        this.arbol.addAtributos(listaAtributos);
    }

    public void addCampos(List<Campo> listaCampos) {
        this.campos.addAll(listaCampos);
    }

    public void addValidaciones(List<Validacion> listaValidaciones) {
        this.validaciones.addAll(listaValidaciones);
    }

    public void addSiguiente(List<Siguiente> listaSiguientes) {
        this.siguientes.addAll(listaSiguientes);
        Iterator it = listaSiguientes.iterator();

        while (it.hasNext()) {
            Siguiente s = (Siguiente) it.next();
            Nodo n = new Nodo(s.getDestino(), this.arbol);
            if (this.arbol.contiene(n)) {
                this.hijos.add(this.arbol.getNodo(s.getDestino()));
            } else {
                this.hijos.add(n);
            }
        }

    }

    public String getTitulo() {
        return this.titulo;
    }

    public List<Nodo> getHijos() {
        return this.hijos;
    }

    public int getNumHijos() {
        int x = this.hijos.size();
        Iterator<Nodo> it = this.hijos.iterator();
        ArrayList nodosRecorridos = new ArrayList();

        while (it.hasNext()) {
            Nodo n = (Nodo) it.next();
            if (!this.contiene(nodosRecorridos, n)) {
                nodosRecorridos.add(n);
                x += n.getNumHijos(nodosRecorridos);
            }
        }

        return x;
    }

    private boolean contiene(List<Nodo> nodosRecorridos, Nodo n) {
        Iterator<Nodo> it = nodosRecorridos.iterator();
        boolean enc = false;

        while (it.hasNext() && !enc) {
            Nodo n1 = (Nodo) it.next();
            if (n.getTitulo().equals(n1.getTitulo())) {
                enc = true;
            }
        }

        return enc;
    }

    private int getNumHijos(List<Nodo> nodosRecorridos) {
        int x = this.hijos.size();
        Iterator it = this.hijos.iterator();

        while (it.hasNext()) {
            Nodo n = (Nodo) it.next();
            if (!this.contiene(nodosRecorridos, n)) {
                nodosRecorridos.add(n);
                x += n.getNumHijos(nodosRecorridos);
            }
        }

        return x;
    }

    public void removeHijo(String destino) {
        Iterator<Nodo> it = this.hijos.iterator();
        boolean enc = false;
        Nodo n = null;

        while (it.hasNext() && !enc) {
            n = (Nodo) it.next();
            if (n.getTitulo().equals(destino)) {
                enc = true;
                this.hijos.remove(n);
            }
        }

    }

    public void removeSiguiente(Siguiente s) {
        this.siguientes.remove(s);
    }

    public void removeValidacion(Validacion v) {
        this.validaciones.remove(v);
    }

    public void removeCampo(Campo c) {
        this.campos.remove(c);
    }

    public void removeAtributo(Atributo a) {
        this.atributos.remove(a);
    }

    public int getNumValidaciones() {
        return this.validaciones.size();
    }

    public void addAtributos(Atributo atr) {
        this.atributos.add(atr);
    }

    public void actualiza(Nodo nd) {
        this.atributos = nd.atributos;
        this.campos = nd.campos;
        this.validaciones = nd.validaciones;
        this.siguientes = nd.siguientes;
        this.hijos = nd.hijos;
    }

    public void addCampo(Campo cmp) {
        this.campos.add(cmp);
    }

    public void addValidacion(Validacion val) {
        this.validaciones.add(val);
    }

    public void addHijo(Nodo hijo) {
        if (this.arbol.contiene(hijo)) {
            this.hijos.add(this.arbol.getNodo(hijo.getTitulo()));
        } else {
            this.hijos.add(hijo);
        }

    }

    public void addSiguiente(Siguiente sgt) {
        this.siguientes.add(sgt);
    }

    public int getNumHijosDirectos() {
        return this.hijos.size();
    }

    public Nodo getPrimeHijo() {
        return (Nodo) this.hijos.get(0);
    }
}
