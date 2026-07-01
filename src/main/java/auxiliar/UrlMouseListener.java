package auxiliar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlMouseListener implements MouseListener {
    private final JLabel etiqueta;
    private final String url;

    public UrlMouseListener(JLabel etiqueta, String url) {
        this.etiqueta = etiqueta;
        this.url = url;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            Logger.error("Abrir enlace", e);
        } catch (RuntimeException e) {
            Logger.error("Abrir enlace en escritorio", e);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        etiqueta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        etiqueta.setCursor(null);
    }
}
