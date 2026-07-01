package auxiliar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class UtilidadesFichero {
    private static final String HOME = System.getProperty("user.home");
    private static final String APP_NAME = "ADS Generator";
    private static final String LOGS_DIR_OVERRIDE = "ads.logs.dir";

    private UtilidadesFichero() {
    }

    public static Path getLogDir() {
        String override = System.getProperty(LOGS_DIR_OVERRIDE);
        if (override != null && !override.isBlank()) {
            return Path.of(override);
        }

        String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (osName.contains("win")) {
            String base = System.getenv("LOCALAPPDATA");
            if (base == null || base.isBlank()) {
                base = System.getenv("APPDATA");
            }
            if (base == null || base.isBlank()) {
                base = HOME;
            }
            return Path.of(base, APP_NAME, "logs");
        }
        if (osName.contains("mac")) {
            return Path.of(HOME, "Library", "Application Support", APP_NAME, "logs");
        }
        return Path.of(HOME, ".local", "share", APP_NAME, "logs");
    }

    public static void createLogFolder() {
        try {
            Files.createDirectories(getLogDir());
        } catch (IOException e) {
            System.err.println("No se pudo crear la carpeta de logs: " + e.getMessage());
        }
    }
}
