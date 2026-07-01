package gui;

import auxiliar.IconUtils;
import auxiliar.UrlMouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.Serial;

public class DialogoAcerca extends JDialog {
    @Serial
    private static final long serialVersionUID = 2L;
    private static final String APP_NAME = "ADS Generator";
    private static final String APP_ICON = "ADS2.png";
    private static final String GPL_ICON = "gplv3-with-text-136x68.png";
    private static final String AUTHOR_WEB = "https://www.jcprieto.es";
    private static final String AUTHOR_EMAIL = "JuanC.Prieto.Silos@gmail.com";
    private static final String GPL_URL = "https://www.gnu.org/licenses/gpl-3.0.html";

    public DialogoAcerca(Ventana ventana) {
        super(ventana, "Acerca de", true);
        super.setIconImage(ventana.getIconImage());
        this.cargarPantalla(ventana);
    }

    private static JLabel crearEtiquetaUrl(String texto, String url) {
        JLabel label = new JLabel(texto, SwingConstants.LEFT);
        label.addMouseListener(new UrlMouseListener(label, url));
        return label;
    }

    private static void addUrlMouseListener(JLabel label, String url) {
        label.addMouseListener(new UrlMouseListener(label, url));
    }

    private void cargarPantalla(Ventana ventana) {
        JPanel panel = new JPanel();
        int yPosition = 0;
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints cns = new GridBagConstraints();

        JLabel titulo = new JLabel("<html><h1>" + APP_NAME + " " + ventana.getVersion() + "</h1></html>",
                IconUtils.loadIconScaled(APP_ICON, 64, 64), SwingConstants.CENTER);
        cns.fill = GridBagConstraints.HORIZONTAL;
        cns.insets = new Insets(10, 10, 10, 10);
        cns.gridx = 0;
        cns.gridy = yPosition++;
        cns.gridwidth = 3;
        panel.add(titulo, cns);

        JLabel creadoPor = new JLabel("Creado por:", SwingConstants.LEFT);
        cns.insets = new Insets(10, 10, 3, 10);
        cns.gridy = yPosition++;
        cns.gridwidth = 1;
        panel.add(creadoPor, cns);

        JLabel autor = new JLabel("<html><b>Juan Carlos Prieto Silos</b></html>", SwingConstants.LEFT);
        cns.insets = new Insets(3, 10, 3, 10);
        cns.gridy = yPosition;
        panel.add(autor, cns);

        JLabel web = crearEtiquetaUrl(AUTHOR_WEB, AUTHOR_WEB);
        cns.gridx = 1;
        panel.add(web, cns);

        JLabel correo = crearEtiquetaUrl(AUTHOR_EMAIL, "mailto:" + AUTHOR_EMAIL + "?subject=ADS_Generator");
        cns.gridx = 2;
        panel.add(correo, cns);

        JLabel poweredBy = new JLabel("Powered by:", SwingConstants.LEFT);
        cns.insets = new Insets(10, 10, 3, 10);
        cns.gridx = 0;
        cns.gridy = ++yPosition;
        cns.gridwidth = 1;
        panel.add(poweredBy, cns);

        addPowered(panel, cns, ++yPosition, "JGraphX", "https://github.com/vlsi/jgraphx");
        addPowered(panel, cns, ++yPosition, "JDOM", "https://www.jdom.org/");
        addPowered(panel, cns, ++yPosition, "SwingX", "https://central.sonatype.com/artifact/org.swinglabs/swingx/1.6.1");
        addPowered(panel, cns, ++yPosition, "Gson", "https://github.com/google/gson");
        addPowered(panel, cns, ++yPosition, "Apache Commons IO", "https://commons.apache.org/proper/commons-io/");

        JLabel licencia = new JLabel("Licencia GNU General Public License v3.0 (GPL-3.0)", IconUtils.loadIcon(GPL_ICON), SwingConstants.CENTER);
        addUrlMouseListener(licencia, GPL_URL);
        cns.insets = new Insets(10, 10, 10, 10);
        cns.gridx = 0;
        cns.gridy = ++yPosition;
        cns.gridwidth = 3;
        panel.add(licencia, cns);

        JButton botonOk = new JButton("Aceptar");
        botonOk.setToolTipText("Aceptar");
        botonOk.addActionListener(al -> this.dispose());
        cns.gridy = ++yPosition;
        panel.add(botonOk, cns);

        super.add(panel);
        super.pack();
        super.setLocationRelativeTo(ventana);
    }

    private void addPowered(JPanel panel, GridBagConstraints cns, int y, String titulo, String url) {
        JLabel etiquetaTitulo = crearEtiquetaUrl("<html><b>" + titulo + "</b></html>", url);
        cns.insets = new Insets(3, 10, 3, 10);
        cns.gridx = 0;
        cns.gridy = y;
        cns.gridwidth = 1;
        panel.add(etiquetaTitulo, cns);

        JLabel etiquetaUrl = crearEtiquetaUrl(url, url);
        cns.gridx = 1;
        cns.gridy = y;
        cns.gridwidth = 2;
        panel.add(etiquetaUrl, cns);
    }
}
