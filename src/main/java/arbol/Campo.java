package arbol;

import java.util.Objects;

public record Campo(String nombre, String tipo, String etiqueta, String valor, String enlace) {
    private static final String[] listaTipos = new String[]{"", "fijo", "imagen", "texto", "tntero", "decimal", "preciso", "fecha", "hora", "booleano", "info", "radio", "desplegable", "lista"};

    public static String[] getListaTipos() {
        return listaTipos;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Campo c)) {
            return false;
        }
        return Objects.equals(this.nombre, c.nombre);
    }

    public int hashCode() {
        return Objects.hash(this.nombre);
    }
}
