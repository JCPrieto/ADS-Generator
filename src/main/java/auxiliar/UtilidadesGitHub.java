package auxiliar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public final class UtilidadesGitHub {
    private static final String REPO_OWNER = "JCPrieto";
    private static final String REPO_NAME = "ADS-Generator";
    private static final String LATEST_RELEASE_URL = "https://api.github.com/repos/" + REPO_OWNER + "/" + REPO_NAME + "/releases/latest";
    private static final int CONNECT_TIMEOUT_MS = 8000;
    private static final int READ_TIMEOUT_MS = 15000;

    private UtilidadesGitHub() {
    }

    public static void comprobarNuevaVersionAsync(javax.swing.JFrame parent, javax.swing.JMenuBar menu, Runnable onDownload) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            private Exception error;

            @Override
            protected Boolean doInBackground() {
                try {
                    return existeNuevaVersion();
                } catch (Exception e) {
                    error = e;
                    return false;
                }
            }

            @Override
            protected void done() {
                if (error != null) {
                    JOptionPane.showMessageDialog(parent, "No se pudo consultar si hay una nueva version.", "Actualizacion", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    if (get()) {
                        menu.add(javax.swing.Box.createHorizontalGlue());
                        javax.swing.JMenuItem jmActualizacion = new javax.swing.JMenuItem("Nueva version disponible");
                        jmActualizacion.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
                        jmActualizacion.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                        jmActualizacion.addActionListener(al -> onDownload.run());
                        menu.add(jmActualizacion);
                        menu.revalidate();
                        menu.repaint();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(parent, "No se pudo consultar si hay una nueva version.", "Actualizacion", JOptionPane.ERROR_MESSAGE);
                } catch (java.util.concurrent.ExecutionException e) {
                    JOptionPane.showMessageDialog(parent, "No se pudo consultar si hay una nueva version.", "Actualizacion", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    public static boolean existeNuevaVersion() throws IOException {
        ReleaseInfo releaseInfo = fetchLatestRelease();
        if (releaseInfo == null || releaseInfo.version == null) {
            return false;
        }
        return diferenteVersion(releaseInfo.version);
    }

    private static boolean diferenteVersion(String serverVersion) {
        String[] sv = serverVersion.split("\\.");
        String[] av = VersionApp.getVersion().split("\\.");
        int max = Math.max(sv.length, av.length);
        for (int i = 0; i < max; i++) {
            int s = i < sv.length ? parseIntSafe(sv[i]) : 0;
            int a = i < av.length ? parseIntSafe(av[i]) : 0;
            if (s > a) {
                return true;
            }
            if (s < a) {
                return false;
            }
        }
        return false;
    }

    private static int parseIntSafe(String value) {
        try {
            String trimmed = value == null ? "" : value.trim();
            StringBuilder digits = new StringBuilder();
            for (int i = 0; i < trimmed.length(); i++) {
                char c = trimmed.charAt(i);
                if (Character.isDigit(c)) {
                    digits.append(c);
                } else {
                    break;
                }
            }
            if (digits.isEmpty()) {
                return 0;
            }
            return Integer.parseInt(digits.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void descargarNuevaVersionAsync(javax.swing.JFrame parent) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retorno = fc.showSaveDialog(parent);
        if (retorno != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File directorio = fc.getSelectedFile();
        parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        SwingWorker<DownloadResult, Void> worker = new SwingWorker<>() {
            private Exception error;

            @Override
            protected DownloadResult doInBackground() {
                try {
                    ReleaseInfo releaseInfo = fetchLatestRelease();
                    if (releaseInfo == null || releaseInfo.downloadUrl == null) {
                        return DownloadResult.notFound();
                    }
                    File destino = new File(directorio.getPath() + FileSystems.getDefault().getSeparator() + releaseInfo.assetName);
                    FileUtils.copyURLToFile(
                            URI.create(releaseInfo.downloadUrl).toURL(),
                            destino,
                            CONNECT_TIMEOUT_MS,
                            READ_TIMEOUT_MS);
                    return DownloadResult.downloaded();
                } catch (Exception e) {
                    error = e;
                    return DownloadResult.failed();
                }
            }

            @Override
            protected void done() {
                parent.setCursor(Cursor.getDefaultCursor());
                if (error instanceof AccessDeniedException) {
                    JOptionPane.showMessageDialog(parent, "No hay permisos de escritura en la carpeta seleccionada.", "Actualizacion", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (error != null) {
                    JOptionPane.showMessageDialog(parent, "Error al descargar la nueva version.", "Actualizacion", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    DownloadResult result = get();
                    if (result.success) {
                        JOptionPane.showMessageDialog(parent, "Nueva version descargada correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(parent, "No se encontro un archivo de descarga en la release.", "Actualizacion", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(parent, "Descarga interrumpida.", "Actualizacion", JOptionPane.ERROR_MESSAGE);
                } catch (java.util.concurrent.ExecutionException e) {
                    JOptionPane.showMessageDialog(parent, "Error al descargar la nueva version.", "Actualizacion", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private static ReleaseInfo fetchLatestRelease() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(LATEST_RELEASE_URL).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        connection.setReadTimeout(READ_TIMEOUT_MS);
        connection.setRequestProperty("User-Agent", REPO_NAME);
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return null;
        }
        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            String tag = getString(json, "tag_name");
            String version = normalizeVersion(tag);
            JsonArray assets = json.getAsJsonArray("assets");
            String os = detectarOs();
            String assetName = getAssetNameForOs(version, os);
            String downloadUrl = getAssetUrl(assets, assetName);
            if (downloadUrl == null) {
                assetName = getFallbackAssetName(assets, os);
                downloadUrl = getAssetUrl(assets, assetName);
            }
            return new ReleaseInfo(version, assetName, downloadUrl);
        }
    }

    private static String getAssetNameForOs(String version, String os) {
        if (version == null || os == null) {
            return null;
        }
        if ("linux".equals(os)) {
            String linuxSuffix = detectarSufijoUbuntu();
            if (linuxSuffix != null) {
                return "ads-generator-" + version + "-linux-" + linuxSuffix + ".deb";
            }
            return "ads-generator-" + version + "-linux.deb";
        }
        return "ads-generator-" + version + "-" + os + ".zip";
    }

    private static String getFallbackAssetName(JsonArray assets, String os) {
        if (assets == null) {
            return null;
        }
        if ("linux".equals(os)) {
            String debName = getFirstAssetWithExtension(assets, ".deb");
            if (debName != null) {
                return debName;
            }
        }
        return getFirstAssetWithExtension(assets, ".zip");
    }

    private static String getFirstAssetWithExtension(JsonArray assets, String extension) {
        for (int i = 0; i < assets.size(); i++) {
            JsonObject asset = assets.get(i).getAsJsonObject();
            String name = getString(asset, "name");
            if (name != null && name.endsWith(extension)) {
                return name;
            }
        }
        return null;
    }

    private static String getAssetUrl(JsonArray assets, String assetName) {
        if (assets == null || assetName == null) {
            return null;
        }
        for (int i = 0; i < assets.size(); i++) {
            JsonObject asset = assets.get(i).getAsJsonObject();
            String name = getString(asset, "name");
            if (assetName.equals(name)) {
                return getString(asset, "browser_download_url");
            }
        }
        return null;
    }

    private static String getString(JsonObject obj, String key) {
        if (obj == null || !obj.has(key) || obj.get(key).isJsonNull()) {
            return null;
        }
        return obj.get(key).getAsString();
    }

    private static String normalizeVersion(String tagName) {
        if (tagName == null) {
            return null;
        }
        if (tagName.startsWith("v")) {
            return tagName.substring(1);
        }
        return tagName;
    }

    private static String detectarOs() {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            return "windows";
        }
        if (os.contains("mac")) {
            return "mac";
        }
        return "linux";
    }

    private static String detectarSufijoUbuntu() {
        Path osRelease = Path.of("/etc/os-release");
        if (!Files.isReadable(osRelease)) {
            return null;
        }
        try {
            String id = null;
            String versionId = null;
            for (String line : Files.readAllLines(osRelease, StandardCharsets.UTF_8)) {
                if (line.startsWith("ID=")) {
                    id = limpiarValorOsRelease(line.substring("ID=".length()));
                } else if (line.startsWith("VERSION_ID=")) {
                    versionId = limpiarValorOsRelease(line.substring("VERSION_ID=".length()));
                }
            }
            if (!"ubuntu".equals(id) || versionId == null) {
                return null;
            }
            if (versionId.startsWith("22.04")) {
                return "ubuntu22.04";
            }
            if (versionId.startsWith("24.04")) {
                return "ubuntu24.04";
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private static String limpiarValorOsRelease(String value) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    private record ReleaseInfo(String version, String assetName, String downloadUrl) {
    }

    private record DownloadResult(boolean success) {
        private static DownloadResult downloaded() {
            return new DownloadResult(true);
        }

        private static DownloadResult notFound() {
            return new DownloadResult(false);
        }

        private static DownloadResult failed() {
            return new DownloadResult(false);
        }
    }
}
