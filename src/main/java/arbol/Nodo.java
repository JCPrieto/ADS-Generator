package arbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Nodo {
    private final String titulo;
    private List<Nodo> hijos;
    private List<Campo> campos;
    private List<Atributo> atributos;
    private final Arbol arbol;
    private List<Validacion> validaciones;
    private List<Siguiente> siguientes;

    public Nodo(String string, Arbol arbol) {
        this.titulo = string.replace(" ", "_");
        this.hijos = new ArrayList<>();
        this.campos = new ArrayList<>();
        this.atributos = new ArrayList<>();
        this.validaciones = new ArrayList<>();
        this.siguientes = new ArrayList<>();
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
        for (Atributo atributo : listaAtributos) {
            this.addAtributos(atributo);
        }
        this.arbol.addAtributos(listaAtributos);
    }

    public void addCampos(List<Campo> listaCampos) {
        for (Campo campo : listaCampos) {
            this.addCampo(campo);
        }
    }

    public void addValidaciones(List<Validacion> listaValidaciones) {
        for (Validacion validacion : listaValidaciones) {
            this.addValidacion(validacion);
        }
    }

    public void addSiguiente(List<Siguiente> listaSiguientes) {

        for (Siguiente s : listaSiguientes) {
            this.addSiguiente(s);
            Nodo n = new Nodo(s.destino(), this.arbol);
            if (this.arbol.contiene(n)) {
                this.addHijo(this.arbol.getNodo(s.destino()));
            } else {
                this.addHijo(n);
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
        List<Nodo> nodosRecorridos = new ArrayList<>();

        while (it.hasNext()) {
            Nodo n = it.next();
            if (this.noContiene(nodosRecorridos, n)) {
                nodosRecorridos.add(n);
                x += n.getNumHijos(nodosRecorridos);
            }
        }

        return x;
    }

    private boolean noContiene(List<Nodo> nodosRecorridos, Nodo n) {
        Iterator<Nodo> it = nodosRecorridos.iterator();
        boolean enc = false;

        while (it.hasNext() && !enc) {
            Nodo n1 = it.next();
            if (n.getTitulo().equals(n1.getTitulo())) {
                enc = true;
            }
        }

        return !enc;
    }

    private int getNumHijos(List<Nodo> nodosRecorridos) {
        int x = this.hijos.size();

        for (Nodo n : this.hijos) {
            if (this.noContiene(nodosRecorridos, n)) {
                nodosRecorridos.add(n);
                x += n.getNumHijos(nodosRecorridos);
            }
        }

        return x;
    }

    public void removeHijo(String destino) {
        Iterator<Nodo> it = this.hijos.iterator();
        boolean enc = false;

        while (it.hasNext() && !enc) {
            Nodo n = it.next();
            if (n.getTitulo().equals(destino)) {
                enc = true;
                it.remove();
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
        if (!this.atributos.contains(atr)) {
            this.atributos.add(atr);
        }
    }

    public void actualiza(Nodo nd) {
        this.atributos = nd.atributos;
        this.campos = nd.campos;
        this.validaciones = nd.validaciones;
        this.siguientes = nd.siguientes;
        this.hijos = nd.hijos;
    }

    public void addCampo(Campo cmp) {
        if (!this.campos.contains(cmp)) {
            this.campos.add(cmp);
        }
    }

    public void addValidacion(Validacion val) {
        if (!this.validaciones.contains(val)) {
            this.validaciones.add(val);
        }
    }

    public void addHijo(Nodo hijo) {
        Nodo nodo = hijo;
        if (this.arbol.contiene(hijo)) {
            nodo = this.arbol.getNodo(hijo.getTitulo());
        }
        if (this.noContiene(this.hijos, nodo)) {
            this.hijos.add(nodo);
        }

    }

    public void addSiguiente(Siguiente sgt) {
        if (!this.siguientes.contains(sgt)) {
            this.siguientes.add(sgt);
        }
    }

}
