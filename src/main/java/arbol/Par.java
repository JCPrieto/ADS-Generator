package arbol;

public class Par {
    private Nodo nodo;
    private Object vertice;

    public Par(Nodo n, Object destino) {
        this.nodo = n;
        this.vertice = destino;
    }

    public Nodo getNodo() {
        return this.nodo;
    }

    public Object getVertice() {
        return this.vertice;
    }

    public int getLongitud() {
        return this.nodo.toString().length() * 8;
    }
}
