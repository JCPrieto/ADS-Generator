package arbol;

public class Validacion {
    private String condicion;
    private String mensaje;

    public Validacion(String condicion2, String mensaje2) {
        this.condicion = condicion2;
        this.mensaje = mensaje2;
    }

    public String getCondicion() {
        return this.condicion;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public boolean equals(Object o) {
        Validacion v = (Validacion) o;
        return this.condicion.equals(v.condicion);
    }
}
