package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DialogoAcerca extends JDialog implements ActionListener, MouseListener {
    private static final long serialVersionUID = 2L;
    private JLabel etq1;
    private JLabel etq2;
    private JLabel etq3;
    private JButton botonOk;
    private JPanel panel;

    public DialogoAcerca(Ventana ventana) {
        super(ventana);
        super.setIconImage((new ImageIcon(ventana.getIcono())).getImage());
        this.panel = new JPanel();
        this.panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.panel.setLayout(new BoxLayout(this.panel, 1));
        this.etq1 = new JLabel("ADS Generator " + ventana.getVersion());
        this.etq1.setAlignmentX(0.5F);
        this.etq2 = new JLabel("Creado por: Juan Carlos Prieto Silos");
        this.etq2.setAlignmentX(0.5F);
        this.etq3 = new JLabel("JuanC.Prieto.Silos@gmail.com");
        this.etq3.setAlignmentX(0.5F);
        this.etq3.setForeground(Color.blue);
        this.etq3.addMouseListener(this);
        this.botonOk = new JButton("Aceptar");
        this.botonOk.setAlignmentX(0.5F);
        this.botonOk.addActionListener(this);
        this.panel.add(this.etq1);
        this.panel.add(new JLabel(" "));
        this.panel.add(this.etq2);
        this.panel.add(new JLabel(" "));
        this.panel.add(this.etq3);
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

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        this.setCursor(new Cursor(12));
    }

    public void mouseExited(MouseEvent e) {
        this.setCursor(new Cursor(0));
    }

    public void mousePressed(MouseEvent e) {
        try {
            Desktop.getDesktop().browse(new URI("mailto:juanc.prieto.ext@juntadeandalucia.es?subject=ADS_Generator"));
            this.etq3.setForeground(Color.red);
        } catch (IOException var3) {
            var3.printStackTrace();
        } catch (URISyntaxException var4) {
            var4.printStackTrace();
        }

    }

    public void mouseReleased(MouseEvent e) {
    }
}
