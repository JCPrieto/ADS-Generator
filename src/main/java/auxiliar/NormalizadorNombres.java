package auxiliar;

public final class NormalizadorNombres {
    private NormalizadorNombres() {
    }

    public static String paraIdentificador(String nombre) {
        return valorSeguro(nombre).replace(" ", "_");
    }

    public static String paraElementoXml(String nombre) {
        String valor = valorSeguro(nombre);
        if (empiezaPorDigito(valor)) {
            valor = "_" + valor;
        }
        return paraIdentificador(valor);
    }

    public static String desdeElementoXml(String nombre) {
        String valor = valorSeguro(nombre);
        if (valor.startsWith("_")) {
            valor = valor.substring(1);
        }
        return valor.replace("_", " ");
    }

    private static boolean empiezaPorDigito(String valor) {
        return !valor.isEmpty() && Character.isDigit(valor.charAt(0));
    }

    private static String valorSeguro(String valor) {
        return valor == null ? "" : valor;
    }
}
