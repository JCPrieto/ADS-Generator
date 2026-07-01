package gui;

import arbol.Arbol;
import arbol.Atributo;
import arbol.Campo;
import arbol.Nodo;
import auxiliar.ADSFilter;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileGenerator {
    private final Ventana contenedor;
    private final List<Nodo> agregados;
    private Writer out;

    public FileGenerator(Ventana ventana) {
        this.contenedor = ventana;
        this.agregados = new ArrayList<>();
    }

    public void generar(Arbol arbol) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ADSFilter());
        int c = fc.showSaveDialog(this.contenedor);
        if (c != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fc.getSelectedFile();
        File outputFile;
        if (!file.getName().endsWith(".ads")) {
            outputFile = new File(file.getAbsolutePath() + ".ads");
        } else {
            outputFile = new File(file.getAbsolutePath());
        }

        this.agregados.clear();
        try (Writer writer = Files.newBufferedWriter(outputFile.toPath(), StandardCharsets.UTF_8)) {
            this.out = writer;
            this.out.write("Arbol {\n\n");
            Nodo raiz = arbol.getRaiz();
            this.out.write("\ttitulo \"" + arbol.getNombre() + "\"\n\n");
            this.agregados.add(raiz);
            this.escribirNodo(raiz);
            this.out.write("\n}");
        } catch (IOException var6) {
            JOptionPane.showMessageDialog(this.contenedor, "No se pudo generar el archivo ADS.", "Generar archivo", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.out = null;
        }

    }

    private void escribirNodo(Nodo raiz) throws IOException {
        this.out.write("\tnodo " + raiz.getTitulo() + "{\n\n");
        Iterator<Atributo> it = raiz.getAtributos();

        while (it.hasNext()) {
            arbol.Atributo a = it.next();
            this.escribirAtributo(a);
        }

        this.out.write("\n");
        Iterator<Campo> it2 = raiz.getCampos();

        while (it2.hasNext()) {
            arbol.Campo c = it2.next();
            this.escribirCampo(c);
        }

        this.out.write("\n\n");
        Iterator<?> it4;
        if (raiz.getNumValidaciones() != 0) {
            this.out.write("\t\tvalidacion {\n");
            it4 = raiz.getValidaciones();

            while (it4.hasNext()) {
                arbol.Validacion v = (arbol.Validacion) it4.next();
                this.escribirValidacion(v);
            }

            this.out.write("\t\t}\n\n");
        }

        if (raiz.getNumHijos() != 0) {
            this.out.write("\t\tsiguiente {\n");
            it4 = raiz.getSiguientes();

            while (it4.hasNext()) {
                arbol.Siguiente s = (arbol.Siguiente) it4.next();
                this.escribirSiguientes(s);
            }

            this.out.write("\t\t}\n\n\t}\n\n");

            for (Nodo n : raiz.getHijos()) {
                if (!this.escrito(n)) {
                    this.agregados.add(n);
                    this.escribirNodo(n);
                }
            }
        } else {
            this.out.write("\t}\n\n");
        }

    }

    private boolean escrito(Nodo n) {
        Iterator<Nodo> it = this.agregados.iterator();
        boolean enc = false;

        while (it.hasNext() && !enc) {
            Nodo n2 = it.next();
            if (n.getTitulo().equals(n2.getTitulo())) {
                enc = true;
            }
        }

        return enc;
    }

    private void escribirSiguientes(arbol.Siguiente s) throws IOException {
        if (!s.condicion().isEmpty()) {
            this.out.write("\t\t\tsi " + s.condicion() + " entonces " + this.conversion(s.destino()) + "\n");
        } else {
            this.out.write("\t\t\tentonces " + this.conversion(s.destino()) + "\n");
        }

    }

    private void escribirValidacion(arbol.Validacion v) throws IOException {
        this.out.write("\t\t\tsi " + v.condicion() + " mensaje \"" + v.mensaje() + "\"\n");
    }

    private void escribirCampo(arbol.Campo c) throws IOException {
        this.out.write("\t\tcampo " + this.conversion(c.nombre()) + "\n\t\t\ttipo " + c.tipo().toLowerCase() + "\n\t\t\tetiqueta \"" + c.etiqueta() + "\"");
        if (c.tipo().equals("radio")) {
            this.out.write("\n\t\t\topciones [" + this.opciones(c.valor()) + "]");
        } else if (!c.tipo().equals("desplegable") && !c.tipo().equals("lista")) {
            if (!c.valor().isEmpty()) {
                if (this.contenedor.getArbol().contieneAtributo(c.valor())) {
                    this.out.write("\n\t\t\tvalor " + c.valor());
                } else {
                    this.out.write("\n\t\t\tvalor \"" + c.valor() + "\"");
                }
            }
        } else {
            this.out.write("\n\t\t\topciones [\"\"," + this.opciones(c.valor()) + "]");
        }

        if ((c.tipo().equals("fijo") || c.tipo().equals("imagen")) && !c.etiqueta().isEmpty()) {
            this.out.write("\n\t\t\tenlaza \"" + c.enlace() + "\"");
        }

        this.out.write("\n");
    }

    private String opciones(String valor) {
        String s = valor.replace("\n", "");
        String[] s1 = s.split(",");
        StringBuilder f = new StringBuilder();

        for (int i = 0; i < s1.length; ++i) {
            f.append("\"").append(s1[i]).append("\"");
            if (i != s1.length - 1) {
                f.append(",");
            }
        }

        return f.toString();
    }

    private void escribirAtributo(arbol.Atributo a) throws IOException {
        this.out.write("\t\tatributo " + a.nombre());
        if (!a.descripcion().isEmpty()) {
            this.out.write("\n\t\t\tdescripcion \"" + a.descripcion() + "\"");
        }

        if (this.contenedor.getArbol().contieneAtributo(a.valor())) {
            this.out.write("\n\t\t\tvalor " + a.valor() + "\n");
        } else {
            this.out.write("\n\t\t\tvalor \"" + a.valor() + "\"\n");
        }

    }

    private String conversion(String nombre) {
        return nombre.replace(" ", "_");
    }
}
