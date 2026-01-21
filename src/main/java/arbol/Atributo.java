package arbol;

public class Atributo {
    private String nombre;
    private String valor;
    private String descripcion;

    public Atributo(String nombre2, String valor2, String descripcion2) {
        this.nombre = nombre2;
        this.valor = valor2;
        this.descripcion = descripcion2;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getValor() {
        return this.valor;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public boolean equals(Object o) {
        Atributo a = (Atributo) o;
        boolean b;
        if (this.nombre.equals(a.nombre)) {
            b = true;
        } else {
            b = false;
        }

        return b;
    }
}
