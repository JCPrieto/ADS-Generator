package gui;

import arbol.Arbol;
import auxiliar.UtilidadesGitHub;
import auxiliar.VersionApp;
import data.Proyecto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Ventana extends JFrame implements ActionListener, WindowListener {
    private static final long serialVersionUID = 2L;
    private static final String ICONO_RECURSO = "/img/icons/ADS2.png";
    private JMenuBar barraMenu;
    private JMenu menuArchivo;
    private JMenuItem menuNuevo;
    private JMenuItem menuAbrir;
    private JMenuItem menuSalir;
    private JMenu menuAyuda;
    private JMenuItem menuACercaDe;
    private JSplitPane split;
    private Grafo panelGrafo;
    private Editor panelEditor;
    private Arbol arbol;
    private Proyecto p;
    private JMenuItem menuGuardar;

    public Ventana() {
        super("ADS Generator " + VersionApp.getVersion());
        java.net.URL iconUrl = Ventana.class.getResource(ICONO_RECURSO);
        if (iconUrl != null) {
            super.setIconImage((new ImageIcon(iconUrl)).getImage());
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException var2) {
            var2.printStackTrace();
        } catch (InstantiationException var3) {
            var3.printStackTrace();
        } catch (IllegalAccessException var4) {
            var4.printStackTrace();
        } catch (UnsupportedLookAndFeelException var5) {
            var5.printStackTrace();
        }

        super.setDefaultCloseOperation(0);
        this.p = new Proyecto();
        this.cargarBarraMenu();
        this.split = new JSplitPane();
        this.panelGrafo = new Grafo(this);
        JScrollPane scrollGrafo = new JScrollPane();
        scrollGrafo.setViewportView(this.panelGrafo);
        this.panelEditor = new Editor(this);
        this.split.setLeftComponent(scrollGrafo);
        this.split.setRightComponent(this.panelEditor);
        this.split.setDividerLocation(600);
        super.add(this.split);
        this.addWindowListener(this);
        this.comprobarNuevaVersion(this.barraMenu);
    }

    private void cargarBarraMenu() {
        this.barraMenu = new JMenuBar();
        this.menuArchivo = new JMenu("Archivo");
        this.menuNuevo = new JMenuItem("Nuevo proyecto");
        this.menuNuevo.addActionListener(this);
        this.menuAbrir = new JMenuItem("Abrir");
        this.menuAbrir.addActionListener(this);
        this.menuGuardar = new JMenuItem("Guardar Proyecto");
        this.menuGuardar.addActionListener(this);
        this.menuSalir = new JMenuItem("Salir");
        this.menuSalir.addActionListener(this);
        this.menuArchivo.add(this.menuNuevo);
        this.menuArchivo.add(this.menuAbrir);
        this.menuArchivo.add(this.menuGuardar);
        this.menuArchivo.add(this.menuSalir);
        this.menuAyuda = new JMenu("Ayuda");
        this.menuACercaDe = new JMenuItem("A cerca de...");
        this.menuACercaDe.addActionListener(this);
        this.menuAyuda.add(this.menuACercaDe);
        this.barraMenu.add(this.menuArchivo);
        this.barraMenu.add(this.menuAyuda);
        super.setJMenuBar(this.barraMenu);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.menuNuevo) {
            this.crearProyecto();
        }

        if (e.getSource() == this.menuAbrir) {
            this.abrirProyecto();
        }

        if (e.getSource() == this.menuSalir) {
            if (!this.p.getSaved()) {
                this.p.mostrarSalvado(this);
            } else {
                super.dispose();
            }
        }

        if (e.getSource() == this.menuACercaDe) {
            this.mostrarAcercaDe();
        }

        if (e.getSource() == this.menuGuardar && !this.p.vacio() && !this.p.getSaved()) {
            this.p.guardar();
        }

    }

    private void mostrarAcercaDe() {
        DialogoAcerca d = new DialogoAcerca(this);
        d.setVisible(true);
    }

    private void abrirProyecto() {
        AbrirProyecto ap = new AbrirProyecto(this);
        ap.setVisible(true);
    }

    private void crearProyecto() {
        NuevoProyecto np = new NuevoProyecto(this);
        np.setVisible(true);
    }

    public void crearProyecto(String text) {
        this.panelGrafo.setBorder(BorderFactory.createTitledBorder(text));
        this.arbol = new Arbol(text);
        this.panelGrafo.crearArbol(this.arbol);
        this.panelEditor.crearArbol(this.arbol);
        this.p.setArbol(this.arbol);
    }

    public void actualizarGrafo() {
        this.p.SetSaved(false);
        this.panelGrafo.actualizar();
        this.panelGrafo.paintAll(this.panelGrafo.getGraphics());
    }

    public void generarArchivo() {
        FileGenerator f = new FileGenerator(this);
        f.generar(this.arbol);
    }

    private void comprobarNuevaVersion(JMenuBar menu) {
        UtilidadesGitHub.comprobarNuevaVersionAsync(this, menu, this::descargarNuevaVersion);
    }

    private void descargarNuevaVersion() {
        try {
            UtilidadesGitHub.descargarNuevaVersion(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            JOptionPane.showMessageDialog(this, "Descarga interrumpida.", "Actualizacion", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        if (!this.p.getSaved()) {
            this.p.mostrarSalvado(this);
        } else {
            super.dispose();
        }

    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void abrirProyecto(String selectedValue) {
        this.p = new Proyecto(selectedValue);
        this.arbol = this.p.getArbol();
        this.panelGrafo.crearArbol(this.arbol);
        this.panelEditor.crearArbol(this.arbol);
    }

    public String getVersion() {
        return VersionApp.getVersion();
    }

    public String getIcono() {
        return ICONO_RECURSO;
    }

    public Arbol getArbol() {
        return this.arbol;
    }
}
