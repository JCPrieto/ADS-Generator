package arbol;

public class Campo {
    private static String[] listaTipos = new String[]{"", "fijo", "imagen", "texto", "tntero", "decimal", "preciso", "fecha", "hora", "booleano", "info", "radio", "desplegable", "lista"};
    private String nombre;
    private String tipo;
    private String etiqueta;
    private String valor;
    private String enlace;

    public Campo(String nombre2, String valor2, String etiqueta2, String valor3, String string) {
        this.nombre = nombre2;
        this.tipo = valor2;
        this.etiqueta = etiqueta2;
        this.valor = valor3;
        this.enlace = string;
    }

    public static String[] getListaTipos() {
        return listaTipos;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getEtiqueta() {
        return this.etiqueta;
    }

    public String getValor() {
        return this.valor;
    }

    public String getEnlace() {
        return this.enlace;
    }

    public boolean equals(Object o) {
        Campo c = (Campo) o;
        boolean b;
        if (this.nombre.equals(c.nombre)) {
            b = true;
        } else {
            b = false;
        }

        return b;
    }
}
