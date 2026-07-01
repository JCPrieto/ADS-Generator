package gui;

import auxiliar.Logger;

import javax.swing.*;

public class Principal {
    public static void main(String[] args) {
        Logger.init();
        Logger.eliminarLogsVacios();
        SwingUtilities.invokeLater(() -> {
            Ventana v = new Ventana();
            v.setExtendedState(JFrame.MAXIMIZED_BOTH);
            v.setVisible(true);
        });
    }
}
