package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Advertencia extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel panel = new JPanel();
    private JLabel etq;
    private JButton botonOk;

    public Advertencia(String string) {
        this.panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.panel.setLayout(new BoxLayout(this.panel, 1));
        this.etq = new JLabel(string);
        this.etq.setAlignmentX(0.5F);
        this.botonOk = new JButton("Aceptar");
        this.botonOk.setAlignmentX(0.5F);
        this.botonOk.addActionListener(this);
        this.panel.add(this.etq);
        this.panel.add(new JLabel(" "));
        this.panel.add(this.botonOk);
        super.add(this.panel);
        super.pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.botonOk) {
            this.dispose();
        }

    }
}
