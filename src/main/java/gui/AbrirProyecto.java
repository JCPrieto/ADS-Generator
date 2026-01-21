package gui;

import auxiliar.NoHayFicherosException;
import auxiliar.PDSFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AbrirProyecto extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton botonCancelar;
    private JButton botonOk;
    private Ventana contenedor;
    private JList<Object> l;

    public AbrirProyecto(Ventana ventana) {
        super(ventana);
        super.setTitle("Cargar Proyecto");
        this.contenedor = ventana;
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = 1;
        super.setLayout(grid);

        try {
            this.l = new JList(this.obtenerProyectos());
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.weighty = 1.0D;
            grid.setConstraints(this.l, c);
            this.botonOk = new JButton("Ok");
            this.botonOk.addActionListener(this);
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 1;
            c.weighty = 0.0D;
            grid.setConstraints(this.botonOk, c);
            this.botonCancelar = new JButton("Cancelar");
            this.botonCancelar.addActionListener(this);
            c.gridx = 1;
            c.gridy = 1;
            grid.setConstraints(this.botonCancelar, c);
            super.add(this.l);
            super.add(this.botonOk);
            super.add(this.botonCancelar);
        } catch (NoHayFicherosException var7) {
            String s = var7.getMessage();
            JLabel label = new JLabel(s);
            c.gridx = 0;
            c.gridy = 0;
            grid.setConstraints(label, c);
            this.botonCancelar = new JButton("Ok");
            this.botonCancelar.addActionListener(this);
            c.gridx = 0;
            c.gridy = 1;
            grid.setConstraints(this.botonCancelar, c);
            super.add(label);
            super.add(this.botonCancelar);
        }

        super.pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.botonOk && this.l.getSelectedValue() != null) {
            this.contenedor.abrirProyecto((String) this.l.getSelectedValue());
            super.dispose();
        }

        if (e.getSource() == this.botonCancelar) {
            super.dispose();
        }

    }

    private String[] getNombres(String[] ficheros) {
        String[] s = new String[ficheros.length];

        for (int i = 0; i < s.length; ++i) {
            s[i] = ficheros[i].replace(".pds", "");
        }

        return s;
    }

    private String[] obtenerProyectos() throws NoHayFicherosException {
        File dir = new File(".");
        String[] ficheros = dir.list(new PDSFilter());
        if (ficheros != null && ficheros.length != 0) {
            return this.getNombres(ficheros);
        } else {
            throw new NoHayFicherosException("No hay ficheros de proyectos");
        }
    }
}
