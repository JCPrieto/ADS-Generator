package gui;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class Atributo extends JPanel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Editor contenedor;
    private final JTextField nombre;
    private final JTextField valor;
    private final JTextField descripcion;
    private final JButton botonOk;
    private final JButton botonRemove;
    private final JButton botonModificar;
    private boolean modificacion;

    public Atributo(arbol.Atributo a, Editor editor) {
        this(editor);
        this.nombre.setText(a.nombre());
        this.nombre.setEditable(false);
        this.valor.setText(a.valor());
        this.valor.setEditable(false);
        this.descripcion.setText(a.descripcion());
        this.descripcion.setEditable(false);
        this.botonOk.setEnabled(false);
        this.botonModificar.setEnabled(true);
        this.botonRemove.setEnabled(true);
    }

    public Atributo(Editor editor) {
        this.modificacion = false;
        this.contenedor = editor;
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.insets = new Insets(5, 5, 5, 5);
        super.setLayout(g);
        JLabel etqNombre = new JLabel("Nombre: ");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.0D;
        g.setConstraints(etqNombre, c);
        this.nombre = new JTextField();
        AutoCompleteDecorator.decorate(this.nombre, this.contenedor.getAtributos(), false);
        c.gridx = 1;
        c.weightx = 1.0D;
        g.setConstraints(this.nombre, c);
        JLabel etqValor = new JLabel("Valor: ");
        c.gridx = 2;
        c.weightx = 0.0D;
        g.setConstraints(etqValor, c);
        this.valor = new JTextField();
        c.gridx = 3;
        c.weightx = 1.0D;
        g.setConstraints(this.valor, c);
        JLabel etqDescripcion = new JLabel("Descripci�n: ");
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0D;
        g.setConstraints(etqDescripcion, c);
        this.descripcion = new JTextField();
        c.gridx = 1;
        c.gridwidth = 3;
        c.weightx = 1.0D;
        g.setConstraints(this.descripcion, c);
        this.botonOk = new JButton("OK");
        this.botonOk.addActionListener(e -> Atributo.this.addAtributo());
        c.gridx = 4;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.weightx = 0.0D;
        g.setConstraints(this.botonOk, c);
        this.botonModificar = new JButton("Modificar");
        this.botonModificar.addActionListener(e -> Atributo.this.modificarAtributo());
        this.botonModificar.setEnabled(false);
        c.gridx = 5;
        g.setConstraints(this.botonModificar, c);
        this.botonRemove = new JButton("Eliminar");
        this.botonRemove.addActionListener(e -> Atributo.this.removeAtributo());
        this.botonRemove.setEnabled(false);
        c.gridx = 6;
        g.setConstraints(this.botonRemove, c);
        super.add(etqNombre);
        super.add(this.nombre);
        super.add(etqValor);
        super.add(this.valor);
        super.add(etqDescripcion);
        super.add(this.descripcion);
        super.add(this.botonOk);
        super.add(this.botonModificar);
        super.add(this.botonRemove);
    }

    protected void modificarAtributo() {
        this.modificacion = true;
        this.contenedor.actualizarAtributo(this);
        this.nombre.setEditable(true);
        this.valor.setEditable(true);
        this.descripcion.setEditable(true);
        this.botonOk.setEnabled(true);
        this.botonModificar.setEnabled(false);
        this.botonRemove.setEnabled(false);
    }

    protected void removeAtributo() {
        this.contenedor.removeAtributo(this);
    }

    protected void addAtributo() {
        if (!this.nombre.getText().isEmpty()) {
            this.nombre.setEditable(false);
            this.valor.setEditable(false);
            this.descripcion.setEditable(false);
            this.botonOk.setEnabled(false);
            this.botonModificar.setEnabled(true);
            this.botonRemove.setEnabled(true);
            this.contenedor.addAtributo(this);
            this.modificacion = false;
        }

    }

    public String getDescripcion() {
        return this.descripcion.getText();
    }

    public String getNombre() {
        return this.nombre.getText();
    }

    public String getValor() {
        return this.valor.getText();
    }

    public void cancelaAdd() {
        this.nombre.setEditable(true);
        this.valor.setEditable(true);
        this.descripcion.setEditable(true);
        this.botonOk.setEnabled(true);
        this.botonModificar.setEnabled(false);
        this.botonRemove.setEnabled(false);
    }

    public boolean modificado() {
        return this.modificacion;
    }
}
