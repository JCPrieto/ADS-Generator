package gui;

import auxiliar.Logger;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Locale;

public class Principal {
    private static final String APP_WM_CLASS = "es-jcprieto-ADSGenerator";

    public static void main(String[] args) {
        configurarIdentidadLinux();
        Logger.init();
        Logger.eliminarLogsVacios();
        SwingUtilities.invokeLater(() -> {
            Ventana v = new Ventana();
            v.setExtendedState(JFrame.MAXIMIZED_BOTH);
            v.setVisible(true);
        });
    }

    private static void configurarIdentidadLinux() {
        System.setProperty("sun.awt.X11.awtAppClassName",
                System.getProperty("sun.awt.X11.awtAppClassName", APP_WM_CLASS));
        System.setProperty("sun.awt.X11.XWMClass",
                System.getProperty("sun.awt.X11.XWMClass", APP_WM_CLASS));

        String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (!osName.contains("linux") || GraphicsEnvironment.isHeadless()) {
            return;
        }

        try {
            Toolkit.getDefaultToolkit();
            Class<?> xToolkitClass = Class.forName("sun.awt.X11.XToolkit");
            Field awtAppClassName = xToolkitClass.getDeclaredField("awtAppClassName");
            awtAppClassName.setAccessible(true);
            awtAppClassName.set(null, APP_WM_CLASS);
        } catch (ReflectiveOperationException | RuntimeException | LinkageError ignored) {
            // OpenJDK 21 does not expose a public setter for WM_CLASS.
        }
    }
}
