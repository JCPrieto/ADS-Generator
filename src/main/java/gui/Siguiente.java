package gui;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class Siguiente extends JPanel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Editor contenedor;
    private final JTextField condicion;
    private final JButton botonOk;
    private final JButton botonRemove;
    private final JTextField destino;
    private final JButton botonModificar;
    private boolean modificacion;

    public Siguiente(arbol.Siguiente s, Editor editor) {
        this(editor);
        this.condicion.setText(s.condicion());
        this.condicion.setEditable(false);
        this.botonOk.setEnabled(false);
        this.botonModificar.setEnabled(true);
        this.botonRemove.setEnabled(true);
        this.destino.setText(s.destino());
        this.destino.setEditable(false);
    }

    public Siguiente(Editor editor) {
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
        this.botonOk.addActionListener(e -> Siguiente.this.addSiguiente());
        c.gridx = 2;
        c.gridheight = 2;
        c.weightx = 0.0D;
        g.setConstraints(this.botonOk, c);
        this.botonModificar = new JButton("Modificar");
        this.botonModificar.addActionListener(e -> Siguiente.this.modificarSiguiente());
        this.botonModificar.setEnabled(false);
        c.gridx = 3;
        g.setConstraints(this.botonModificar, c);
        this.botonRemove = new JButton("Eliminar");
        this.botonRemove.addActionListener(e -> Siguiente.this.removeSiguiente());
        this.botonRemove.setEnabled(false);
        c.gridx = 4;
        g.setConstraints(this.botonRemove, c);
        JLabel etqDestino = new JLabel("Nodo Destino: ");
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        g.setConstraints(etqDestino, c);
        this.destino = new JTextField();
        AutoCompleteDecorator.decorate(this.destino, this.contenedor.getNodos(), false);
        c.gridx = 1;
        c.weightx = 1.0D;
        g.setConstraints(this.destino, c);
        super.add(etqCondicion);
        super.add(this.condicion);
        super.add(this.botonOk);
        super.add(this.botonModificar);
        super.add(this.botonRemove);
        super.add(etqDestino);
        super.add(this.destino);
    }

    protected void modificarSiguiente() {
        this.modificacion = true;
        this.contenedor.actualizarSiguiente(this);
        this.condicion.setEditable(true);
        this.destino.setEditable(true);
        this.botonOk.setEnabled(true);
        this.botonModificar.setEnabled(false);
        this.botonRemove.setEnabled(false);
    }

    protected void removeSiguiente() {
        this.contenedor.removeSiguiente(this);
    }

    protected void addSiguiente() {
        if (!this.destino.getText().isEmpty()) {
            this.condicion.setEditable(false);
            this.destino.setEditable(false);
            this.botonOk.setEnabled(false);
            this.botonModificar.setEnabled(true);
            this.botonRemove.setEnabled(true);
            this.contenedor.addSiguiente(this);
            this.modificacion = false;
        }

    }

    public String getCondicion() {
        return this.condicion.getText();
    }

    public String getDestino() {
        return this.destino.getText();
    }

    public void cancelaAdd() {
        this.condicion.setEditable(true);
        this.destino.setEditable(true);
        this.destino.setOpaque(true);
        this.botonOk.setEnabled(true);
        this.botonModificar.setEnabled(false);
        this.botonRemove.setEnabled(false);
    }

    public boolean modificado() {
        return this.modificacion;
    }
}
