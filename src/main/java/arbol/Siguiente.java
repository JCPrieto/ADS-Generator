package arbol;

public class Siguiente {
    private String condicion;
    private String destino;

    public Siguiente(String condicion2, String destino2) {
        this.condicion = condicion2;
        this.destino = destino2;
    }

    public String getCondicion() {
        return this.condicion;
    }

    public String getDestino() {
        return this.destino;
    }

    public boolean equals(Object o) {
        Siguiente s = (Siguiente) o;
        return this.condicion.equals(s.condicion);
    }
}
