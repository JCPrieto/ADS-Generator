package arbol;

import java.util.Objects;

public record Atributo(String nombre, String valor, String descripcion) {

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Atributo a)) {
            return false;
        }
        return Objects.equals(this.nombre, a.nombre);
    }

    public int hashCode() {
        return Objects.hash(this.nombre);
    }
}
