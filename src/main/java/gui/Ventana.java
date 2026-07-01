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
import java.io.Serial;

public class Ventana extends JFrame implements ActionListener, WindowListener {
    @Serial
    private static final long serialVersionUID = 2L;
    private static final String ICONO_RECURSO = "/img/icons/ADS2.png";
    private JMenuBar barraMenu;
    private JMenuItem menuNuevo;
    private JMenuItem menuAbrir;
    private JMenuItem menuSalir;
    private JMenuItem menuACercaDe;
    private final Grafo panelGrafo;
    private final Editor panelEditor;
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException var2) {
            var2.printStackTrace();
        }

        super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.p = new Proyecto();
        this.cargarBarraMenu();
        JSplitPane split = new JSplitPane();
        this.panelGrafo = new Grafo(this);
        JScrollPane scrollGrafo = new JScrollPane();
        scrollGrafo.setViewportView(this.panelGrafo);
        this.panelEditor = new Editor(this);
        split.setLeftComponent(scrollGrafo);
        split.setRightComponent(this.panelEditor);
        split.setDividerLocation(600);
        super.add(split);
        this.addWindowListener(this);
        this.comprobarNuevaVersion(this.barraMenu);
    }

    private void cargarBarraMenu() {
        this.barraMenu = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        this.menuNuevo = new JMenuItem("Nuevo proyecto");
        this.menuNuevo.addActionListener(this);
        this.menuAbrir = new JMenuItem("Abrir");
        this.menuAbrir.addActionListener(this);
        this.menuGuardar = new JMenuItem("Guardar Proyecto");
        this.menuGuardar.addActionListener(this);
        this.menuSalir = new JMenuItem("Salir");
        this.menuSalir.addActionListener(this);
        menuArchivo.add(this.menuNuevo);
        menuArchivo.add(this.menuAbrir);
        menuArchivo.add(this.menuGuardar);
        menuArchivo.add(this.menuSalir);
        JMenu menuAyuda = new JMenu("Ayuda");
        this.menuACercaDe = new JMenuItem("A cerca de...");
        this.menuACercaDe.addActionListener(this);
        menuAyuda.add(this.menuACercaDe);
        this.barraMenu.add(menuArchivo);
        this.barraMenu.add(menuAyuda);
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
        this.panelGrafo.revalidate();
        this.panelGrafo.repaint();
    }

    public void generarArchivo() {
        FileGenerator f = new FileGenerator(this);
        f.generar(this.arbol);
    }

    private void comprobarNuevaVersion(JMenuBar menu) {
        UtilidadesGitHub.comprobarNuevaVersionAsync(this, menu, this::descargarNuevaVersion);
    }

    private void descargarNuevaVersion() {
        UtilidadesGitHub.descargarNuevaVersionAsync(this);
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
        Proyecto proyecto = new Proyecto(selectedValue);
        Arbol arbolCargado = proyecto.getArbol();
        if (arbolCargado == null) {
            return;
        }
        this.p = proyecto;
        this.arbol = arbolCargado;
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
