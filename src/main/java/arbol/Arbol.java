package arbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Arbol {
    private String nombre;
    private Nodo raiz;
    private List<String> atributos;

    public Arbol(String text) {
        this.nombre = text;
        this.atributos = new ArrayList();
        this.raiz = new Nodo("Inicio", this);
    }

    public Nodo getRaiz() {
        return this.raiz;
    }

    public void addAtributos(List<Atributo> listaAtributos) {
        Iterator it = listaAtributos.iterator();

        while (it.hasNext()) {
            Atributo a = (Atributo) it.next();
            this.atributos.add(a.getNombre());
        }

    }

    public boolean contiene(Nodo n) {
        boolean enc = false;
        Iterator it = this.getNodos();

        while (it.hasNext() && !enc) {
            Nodo n2 = (Nodo) it.next();
            if (n.getTitulo().equals(n2.getTitulo())) {
                enc = true;
            }
        }

        return enc;
    }

    public Nodo getNodo(String destino) {
        Nodo n = null;
        if (this.raiz.getTitulo().equals(destino)) {
            n = this.raiz;
        } else {
            List<Nodo> l = new ArrayList();
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
            n2 = (Nodo) it.next();
            if (n2.getTitulo().equals(destino)) {
                n = n2;
            } else if (!this.contiene(l, n2)) {
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
        List<Nodo> l = new ArrayList();
        l.add(this.raiz);
        Iterator it = this.raiz.getHijos().iterator();

        while (it.hasNext()) {
            Nodo n = (Nodo) it.next();
            if (!this.contiene(l, n)) {
                l.add(n);
                this.getNodos(l, n);
            }
        }

        return l.iterator();
    }

    private void getNodos(List<Nodo> l, Nodo n) {
        Iterator it = n.getHijos().iterator();

        while (it.hasNext()) {
            Nodo n2 = (Nodo) it.next();
            if (!this.contiene(l, n2)) {
                l.add(n2);
                this.getNodos(l, n2);
            }
        }

    }

    private boolean contiene(List<Nodo> l, Nodo n2) {
        Iterator<Nodo> it = l.iterator();
        boolean enc = false;

        while (it.hasNext() && !enc) {
            Nodo n = (Nodo) it.next();
            if (n.getTitulo().equals(n2.getTitulo())) {
                enc = true;
            }
        }

        return enc;
    }

    public void addNodo(Nodo nd) {
        Nodo n = this.getNodo(nd.getTitulo());
        n.actualiza(nd);
    }

    public void addAtributo(Atributo atr) {
        this.atributos.add(atr.getNombre());
    }

    public boolean contieneAtributo(String valor) {
        return this.atributos.contains(valor);
    }
}
