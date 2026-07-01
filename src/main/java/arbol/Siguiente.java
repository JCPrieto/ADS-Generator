package arbol;

import java.util.Objects;

public record Siguiente(String condicion, String destino) {

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Siguiente s)) {
            return false;
        }
        return Objects.equals(this.condicion, s.condicion);
    }

    public int hashCode() {
        return Objects.hash(this.condicion);
    }
}
