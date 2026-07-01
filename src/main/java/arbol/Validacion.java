package arbol;

import java.util.Objects;

public record Validacion(String condicion, String mensaje) {

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Validacion v)) {
            return false;
        }
        return Objects.equals(this.condicion, v.condicion);
    }

    public int hashCode() {
        return Objects.hash(this.condicion);
    }
}
