package gui;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Campo extends JPanel {
    private static final long serialVersionUID = 1L;
    private JButton botonModificar;
    private JButton botonOk;
    private JButton botonRemove;
    private Editor contenedor;
    private JTextField enlaza;
    private JTextField etiqueta;
    private JLabel etqEnlaza;
    private JLabel etqValor;
    private boolean modificacion;
    private JTextField nombre;
    private JComboBox<?> tipo;
    private JTextArea valor;

    public Campo(arbol.Campo cmp, Editor editor) {
        this(editor);
        this.nombre.setText(cmp.getNombre());
        this.nombre.setEditable(false);
        this.tipo.setSelectedItem(cmp.getTipo());
        this.tipo.setEditable(false);
        this.etiqueta.setText(cmp.getEtiqueta());
        this.etiqueta.setEditable(false);
        this.etqValor.setText(this.getEtqValor((String) this.tipo.getSelectedItem()));
        this.valor.setText(cmp.getValor());
        this.valor.setEditable(false);
        this.enlaza.setText(cmp.getEnlace());
        this.enlaza.setEditable(false);
        this.botonOk.setEnabled(false);
        this.botonModificar.setEnabled(true);
        this.botonRemove.setEnabled(true);
    }

    public Campo(Editor editor) {
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
        JLabel etqTipo = new JLabel("Tipo: ");
        c.gridx = 2;
        c.weightx = 0.0D;
        g.setConstraints(etqTipo, c);
        this.tipo = new JComboBox(arbol.Campo.getListaTipos());
        this.tipo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Campo.this.tipo.getSelectedIndex() != 0) {
                    Campo.this.etqValor.setText(Campo.this.getEtqValor((String) Campo.this.tipo.getSelectedItem()));
                    Campo.this.enlazar((String) Campo.this.tipo.getSelectedItem());
                    Campo.this.repintar();
                }

            }
        });
        c.gridx = 3;
        c.weightx = 1.0D;
        g.setConstraints(this.tipo, c);
        JLabel etqEtiqueta = new JLabel("Etiqueta: ");
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0D;
        g.setConstraints(etqEtiqueta, c);
        this.etiqueta = new JTextField();
        c.gridx = 1;
        c.weightx = 1.0D;
        g.setConstraints(this.etiqueta, c);
        this.etqValor = new JLabel("Valor: ");
        c.gridx = 2;
        c.weightx = 0.0D;
        g.setConstraints(this.etqValor, c);
        this.valor = new JTextArea();
        this.valor.setBorder(BorderFactory.createLoweredBevelBorder());
        c.gridx = 3;
        c.weightx = 1.0D;
        g.setConstraints(this.valor, c);
        this.etqEnlaza = new JLabel("Enlaza: ");
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.0D;
        g.setConstraints(this.etqEnlaza, c);
        this.enlaza = new JTextField();
        this.enlaza.setEditable(false);
        c.gridx = 1;
        c.gridwidth = 5;
        g.setConstraints(this.enlaza, c);
        this.botonOk = new JButton("OK");
        this.botonOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Campo.this.addCampo();
            }
        });
        c.gridx = 4;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.weightx = 0.0D;
        g.setConstraints(this.botonOk, c);
        this.botonModificar = new JButton("Modificar");
        this.botonModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Campo.this.modificarCampo();
            }
        });
        this.botonModificar.setEnabled(false);
        c.gridx = 5;
        g.setConstraints(this.botonModificar, c);
        this.botonRemove = new JButton("Eliminar");
        this.botonRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Campo.this.removeCampo();
            }
        });
        this.botonRemove.setEnabled(false);
        c.gridx = 6;
        g.setConstraints(this.botonRemove, c);
        super.add(etqNombre);
        super.add(this.nombre);
        super.add(etqTipo);
        super.add(this.tipo);
        super.add(etqEtiqueta);
        super.add(this.etiqueta);
        super.add(this.etqValor);
        super.add(this.valor);
        super.add(this.botonOk);
        super.add(this.botonModificar);
        super.add(this.botonRemove);
        super.add(this.etqEnlaza);
        super.add(this.enlaza);
    }

    protected void addCampo() {
        if (!this.nombre.getText().isEmpty() && this.tipo.getSelectedIndex() != -1) {
            this.nombre.setEditable(false);
            this.tipo.setEnabled(false);
            this.etiqueta.setEditable(false);
            this.valor.setEditable(false);
            this.botonOk.setEnabled(false);
            this.botonModificar.setEnabled(true);
            this.botonRemove.setEnabled(true);
            this.enlaza.setEditable(false);
            this.contenedor.addCampo(this);
            this.modificacion = false;
        }

    }

    public void cancelaAdd() {
        this.nombre.setEditable(true);
        this.tipo.setEnabled(true);
        this.etiqueta.setEditable(true);
        this.valor.setEditable(true);
        this.botonOk.setEnabled(true);
        this.botonModificar.setEnabled(false);
        this.botonRemove.setEnabled(false);
        this.enlaza.setEditable(true);
    }

    protected void enlazar(String selectedItem) {
        if (!selectedItem.equals("fijo") && !selectedItem.equals("imagen")) {
            this.enlaza.setText("");
            this.enlaza.setEditable(false);
        } else {
            this.enlaza.setEditable(true);
        }

    }

    public String getEnlace() {
        return !this.tipo.getSelectedItem().equals("fijo") && !this.tipo.getSelectedItem().equals("imagen") ? "" : this.enlaza.getText();
    }

    public String getEtiqueta() {
        return this.etiqueta.getText();
    }

    protected String getEtqValor(String selectedItem) {
        String s;
        if (!selectedItem.equals("radio") && !selectedItem.equals("desplegable") && !selectedItem.equals("lista")) {
            s = "Valor: ";
        } else {
            s = "Opciones: ";
        }

        return s;
    }

    public String getNombre() {
        return this.nombre.getText();
    }

    public String getTipo() {
        return (String) this.tipo.getSelectedItem();
    }

    public String getValor() {
        return this.valor.getText();
    }

    public boolean modificado() {
        return this.modificacion;
    }

    protected void modificarCampo() {
        this.modificacion = true;
        this.contenedor.actualizarCampo(this);
        this.nombre.setEditable(true);
        this.tipo.setEditable(true);
        this.etiqueta.setEditable(true);
        this.valor.setEditable(true);
        this.enlaza.setEditable(true);
        this.botonOk.setEnabled(true);
        this.botonModificar.setEnabled(false);
        this.botonRemove.setEnabled(false);
    }

    protected void removeCampo() {
        this.contenedor.removeCampo(this);
    }

    protected void repintar() {
        this.paintAll(this.getGraphics());
    }
}
