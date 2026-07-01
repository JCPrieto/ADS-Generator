package gui;

import javax.swing.*;

public class Principal {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ventana v = new Ventana();
            v.setExtendedState(JFrame.MAXIMIZED_BOTH);
            v.setVisible(true);
        });
    }
}
