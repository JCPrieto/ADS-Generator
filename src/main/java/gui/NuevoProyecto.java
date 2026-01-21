package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NuevoProyecto extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private Ventana padre;
    private JTextField nombreProyecto;
    private JButton botonOk;
    private JButton botonCancelar;

    public NuevoProyecto(Ventana ventana) {
        super(ventana);
        super.setTitle("Nuevo Proyecto");
        this.padre = ventana;
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        super.setLayout(grid);
        JLabel etiquetaNombreProyecto = new JLabel("Indique el nombre del proyecto:");
        c.gridx = 0;
        c.gridy = 0;
        grid.setConstraints(etiquetaNombreProyecto, c);
        this.nombreProyecto = new JTextField();
        this.nombreProyecto.setColumns(15);
        c.gridx = 1;
        c.gridy = 0;
        grid.setConstraints(this.nombreProyecto, c);
        this.botonOk = new JButton("Ok");
        this.botonOk.addActionListener(this);
        c.gridx = 0;
        c.gridy = 1;
        grid.setConstraints(this.botonOk, c);
        this.botonCancelar = new JButton("Cancelar");
        this.botonCancelar.addActionListener(this);
        c.gridx = 1;
        c.gridy = 1;
        grid.setConstraints(this.botonCancelar, c);
        super.add(etiquetaNombreProyecto);
        super.add(this.nombreProyecto);
        super.add(this.botonOk);
        super.add(this.botonCancelar);
        super.pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.botonOk && !this.nombreProyecto.getText().isEmpty()) {
            this.padre.crearProyecto(this.nombreProyecto.getText());
            this.dispose();
        }

        if (e.getSource() == this.botonCancelar) {
            this.dispose();
        }

    }
}
