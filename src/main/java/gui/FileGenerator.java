package gui;

import arbol.Arbol;
import arbol.Nodo;
import auxiliar.ADSFilter;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileGenerator {
    private FileWriter out;
    private File file;
    private Ventana contenedor;
    private List<Nodo> agregados;

    public FileGenerator(Ventana ventana) {
        this.contenedor = ventana;
        this.agregados = new ArrayList();
    }

    public void generar(Arbol arbol) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ADSFilter());
        int c = fc.showSaveDialog(this.contenedor);
        if (c == 0) {
            this.file = fc.getSelectedFile();
        }

        File outputFile;
        if (!this.file.getName().endsWith(".ads")) {
            outputFile = new File(this.file.getAbsolutePath() + ".ads");
        } else {
            outputFile = new File(this.file.getAbsolutePath());
        }

        try {
            this.out = new FileWriter(outputFile);
            this.out.write("Arbol {\n\n");
            Nodo raiz = arbol.getRaiz();
            this.out.write("\ttitulo \"" + arbol.getNombre() + "\"\n\n");
            this.agregados.add(raiz);
            this.escribirNodo(raiz);
            this.out.write("\n}");
            this.out.close();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

    private void escribirNodo(Nodo raiz) throws IOException {
        this.out.write("\tnodo " + raiz.getTitulo() + "{\n\n");
        Iterator it = raiz.getAtributos();

        while (it.hasNext()) {
            arbol.Atributo a = (arbol.Atributo) it.next();
            this.escribirAtributo(a);
        }

        this.out.write("\n");
        Iterator it2 = raiz.getCampos();

        while (it2.hasNext()) {
            arbol.Campo c = (arbol.Campo) it2.next();
            this.escribirCampo(c);
        }

        this.out.write("\n\n");
        Iterator it4;
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
            Iterator it5 = raiz.getHijos().iterator();

            while (it5.hasNext()) {
                Nodo n = (Nodo) it5.next();
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
            Nodo n2 = (Nodo) it.next();
            if (n.getTitulo().equals(n2.getTitulo())) {
                enc = true;
            }
        }

        return enc;
    }

    private void escribirSiguientes(arbol.Siguiente s) throws IOException {
        if (!s.getCondicion().isEmpty()) {
            this.out.write("\t\t\tsi " + s.getCondicion() + " entonces " + this.conversion(s.getDestino()) + "\n");
        } else {
            this.out.write("\t\t\tentonces " + this.conversion(s.getDestino()) + "\n");
        }

    }

    private void escribirValidacion(arbol.Validacion v) throws IOException {
        this.out.write("\t\t\tsi " + v.getCondicion() + " mensaje \"" + v.getMensaje() + "\"\n");
    }

    private void escribirCampo(arbol.Campo c) throws IOException {
        this.out.write("\t\tcampo " + this.conversion(c.getNombre()) + "\n\t\t\ttipo " + c.getTipo().toLowerCase() + "\n\t\t\tetiqueta \"" + c.getEtiqueta() + "\"");
        if (c.getTipo().equals("radio")) {
            this.out.write("\n\t\t\topciones [" + this.opciones(c.getValor()) + "]");
        } else if (!c.getTipo().equals("desplegable") && !c.getTipo().equals("lista")) {
            if (!c.getValor().isEmpty()) {
                if (this.contenedor.getArbol().contieneAtributo(c.getValor())) {
                    this.out.write("\n\t\t\tvalor " + c.getValor());
                } else {
                    this.out.write("\n\t\t\tvalor \"" + c.getValor() + "\"");
                }
            }
        } else {
            this.out.write("\n\t\t\topciones [\"\"," + this.opciones(c.getValor()) + "]");
        }

        if ((c.getTipo().equals("fijo") || c.getTipo().equals("imagen")) && !c.getEtiqueta().isEmpty()) {
            this.out.write("\n\t\t\tenlaza \"" + c.getEnlace() + "\"");
        }

        this.out.write("\n");
    }

    private String opciones(String valor) {
        String s = valor.replace("\n", "");
        String[] s1 = s.split(",");
        String f = "";

        for (int i = 0; i < s1.length; ++i) {
            f = f + "\"" + s1[i] + "\"";
            if (i != s1.length - 1) {
                f = f + ",";
            }
        }

        return f;
    }

    private void escribirAtributo(arbol.Atributo a) throws IOException {
        this.out.write("\t\tatributo " + a.getNombre());
        if (!a.getDescripcion().isEmpty()) {
            this.out.write("\n\t\t\tdescripcion \"" + a.getDescripcion() + "\"");
        }

        if (this.contenedor.getArbol().contieneAtributo(a.getValor())) {
            this.out.write("\n\t\t\tvalor " + a.getValor() + "\n");
        } else {
            this.out.write("\n\t\t\tvalor \"" + a.getValor() + "\"\n");
        }

    }

    private String conversion(String nombre) {
        return nombre.replace(" ", "_");
    }
}
