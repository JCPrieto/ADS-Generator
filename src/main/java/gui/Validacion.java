package gui;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class Validacion extends JPanel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Editor contenedor;
    private final JTextField condicion;
    private final JButton botonOk;
    private final JButton botonRemove;
    private final JTextArea mensaje;
    private final JButton botonModificar;
    private boolean modificacion;

    public Validacion(arbol.Validacion v, Editor editor) {
        this(editor);
        this.condicion.setText(v.condicion());
        this.condicion.setEditable(false);
        this.botonOk.setEnabled(false);
        this.botonModificar.setEnabled(true);
        this.botonRemove.setEnabled(true);
        this.mensaje.setText(v.mensaje());
        this.mensaje.setEditable(false);
        this.mensaje.setOpaque(false);
    }

    public Validacion(Editor editor) {
        this.modificacion = false;
        this.contenedor = editor;
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.insets = new Insets(5, 5, 5, 5);
        super.setLayout(g);
        c.weighty = 1.0D;
        JLabel etqCondicion = new JLabel("Condici�n: ");
        c.gridx = 0;
        c.weightx = 0.0D;
        g.setConstraints(etqCondicion, c);
        this.condicion = new JTextField();
        c.gridx = 1;
        c.weightx = 1.0D;
        g.setConstraints(this.condicion, c);
        this.botonOk = new JButton("OK");
        this.botonOk.addActionListener(e -> Validacion.this.addValidacion());
        c.gridx = 2;
        c.gridheight = 2;
        c.weightx = 0.0D;
        g.setConstraints(this.botonOk, c);
        this.botonModificar = new JButton("Modificar");
        this.botonModificar.addActionListener(e -> Validacion.this.modificarValidacion());
        this.botonModificar.setEnabled(false);
        c.gridx = 3;
        g.setConstraints(this.botonModificar, c);
        this.botonRemove = new JButton("Eliminar");
        this.botonRemove.addActionListener(e -> Validacion.this.removeValidacion());
        this.botonRemove.setEnabled(false);
        c.gridx = 4;
        g.setConstraints(this.botonRemove, c);
        JLabel etqMensaje = new JLabel("Mensaje: ");
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        g.setConstraints(etqMensaje, c);
        this.mensaje = new JTextArea();
        this.mensaje.setBorder(BorderFactory.createLoweredBevelBorder());
        c.gridx = 1;
        c.weightx = 1.0D;
        g.setConstraints(this.mensaje, c);
        super.add(etqCondicion);
        super.add(this.condicion);
        super.add(this.botonOk);
        super.add(this.botonModificar);
        super.add(this.botonRemove);
        super.add(etqMensaje);
        super.add(this.mensaje);
    }

    protected void modificarValidacion() {
        this.modificacion = true;
        this.contenedor.actualizarValidacion(this);
        this.condicion.setEditable(true);
        this.mensaje.setEditable(true);
        this.botonOk.setEnabled(true);
        this.botonModificar.setEnabled(false);
        this.botonRemove.setEnabled(false);
    }

    protected void removeValidacion() {
        this.contenedor.removeValidacion(this);
    }

    protected void addValidacion() {
        if (!this.condicion.getText().isEmpty() && !this.mensaje.getText().isEmpty()) {
            this.condicion.setEditable(false);
            this.mensaje.setEditable(false);
            this.mensaje.setOpaque(false);
            this.botonOk.setEnabled(false);
            this.botonRemove.setEnabled(true);
            this.contenedor.addValidacion(this);
            this.modificacion = false;
        }

    }

    public String getCondicion() {
        return this.condicion.getText();
    }

    public String getMensaje() {
        return this.mensaje.getText();
    }

    public void cancelaAdd() {
        this.condicion.setEditable(true);
        this.mensaje.setEditable(true);
        this.mensaje.setOpaque(true);
        this.botonOk.setEnabled(true);
        this.botonModificar.setEnabled(false);
        this.botonRemove.setEnabled(false);
    }

    public boolean modificado() {
        return this.modificacion;
    }
}
