package arbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Arbol {
    private final String nombre;
    private final Nodo raiz;
    private final List<String> atributos;

    public Arbol(String text) {
        this.nombre = text;
        this.atributos = new ArrayList<>();
        this.raiz = new Nodo("Inicio", this);
    }

    public Nodo getRaiz() {
        return this.raiz;
    }

    public void addAtributos(List<Atributo> listaAtributos) {

        for (Atributo a : listaAtributos) {
            this.addAtributo(a);
        }

    }

    public boolean contiene(Nodo n) {
        boolean enc = false;
        Iterator<Nodo> it = this.getNodos();

        while (it.hasNext() && !enc) {
            Nodo n2 = it.next();
            if (n.getTitulo().equals(n2.getTitulo())) {
                enc = true;
            }
        }

        return enc;
    }

    public Nodo getNodo(String destino) {
        Nodo n;
        if (this.raiz.getTitulo().equals(destino)) {
            n = this.raiz;
        } else {
            List<Nodo> l = new ArrayList<>();
            l.add(this.raiz);
            n = this.getNodo(this.raiz, destino, l);
        }

        return n;
    }

    private Nodo getNodo(Nodo raiz2, String destino, List<Nodo> l) {
        Iterator<Nodo> it = raiz2.getHijos().iterator();

        Nodo n;
        Nodo n2;
        for (n = null; n == null && it.hasNext(); l.add(n2)) {
            n2 = it.next();
            if (n2.getTitulo().equals(destino)) {
                n = n2;
            } else if (this.noContiene(l, n2)) {
                n = this.getNodo(n2, destino, l);
            }
        }

        return n;
    }

    public String getNombre() {
        return this.nombre;
    }

    public List<String> getAtributos() {
        return this.atributos;
    }

    public Iterator<Nodo> getNodos() {
        List<Nodo> l = new ArrayList<>();
        l.add(this.raiz);

        for (Nodo n : this.raiz.getHijos()) {
            if (this.noContiene(l, n)) {
                l.add(n);
                this.getNodos(l, n);
            }
        }

        return l.iterator();
    }

    private void getNodos(List<Nodo> l, Nodo n) {

        for (Nodo n2 : n.getHijos()) {
            if (this.noContiene(l, n2)) {
                l.add(n2);
                this.getNodos(l, n2);
            }
        }

    }

    private boolean noContiene(List<Nodo> l, Nodo n2) {
        Iterator<Nodo> it = l.iterator();
        boolean enc = false;

        while (it.hasNext() && !enc) {
            Nodo n = it.next();
            if (n.getTitulo().equals(n2.getTitulo())) {
                enc = true;
            }
        }

        return !enc;
    }

    public void addNodo(Nodo nd) {
        Nodo n = this.getNodo(nd.getTitulo());
        n.actualiza(nd);
    }

    public void addAtributo(Atributo atr) {
        if (!this.atributos.contains(atr.nombre())) {
            this.atributos.add(atr.nombre());
        }
    }

    public boolean contieneAtributo(String valor) {
        return this.atributos.contains(valor);
    }
}
