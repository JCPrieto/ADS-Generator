package gui;

import auxiliar.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.Serial;
import java.net.URI;
import java.net.URISyntaxException;

public class DialogoAcerca extends JDialog implements ActionListener, MouseListener {
    @Serial
    private static final long serialVersionUID = 2L;
    private final JLabel etq3;
    private final JButton botonOk;

    public DialogoAcerca(Ventana ventana) {
        super(ventana);
        super.setIconImage((new ImageIcon(ventana.getIcono())).getImage());
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel etq1 = new JLabel("ADS Generator " + ventana.getVersion());
        etq1.setAlignmentX(0.5F);
        JLabel etq2 = new JLabel("Creado por: Juan Carlos Prieto Silos");
        etq2.setAlignmentX(0.5F);
        this.etq3 = new JLabel("JuanC.Prieto.Silos@gmail.com");
        this.etq3.setAlignmentX(0.5F);
        this.etq3.setForeground(Color.blue);
        this.etq3.addMouseListener(this);
        this.botonOk = new JButton("Aceptar");
        this.botonOk.setAlignmentX(0.5F);
        this.botonOk.addActionListener(this);
        panel.add(etq1);
        panel.add(new JLabel(" "));
        panel.add(etq2);
        panel.add(new JLabel(" "));
        panel.add(this.etq3);
        panel.add(new JLabel(" "));
        panel.add(this.botonOk);
        super.add(panel);
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
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void mouseExited(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void mousePressed(MouseEvent e) {
        try {
            Desktop.getDesktop().browse(new URI("mailto:juanc.prieto.ext@juntadeandalucia.es?subject=ADS_Generator"));
            this.etq3.setForeground(Color.red);
        } catch (IOException var3) {
            Logger.error("Abrir cliente de correo", var3);
        } catch (URISyntaxException var4) {
            Logger.error("Crear URI de correo", var4);
        }

    }

    public void mouseReleased(MouseEvent e) {
    }
}
