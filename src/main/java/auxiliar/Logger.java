package auxiliar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public final class Logger {
    private static final String FECHA = LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    private static final int LOG_ROTATION_SIZE_BYTES = 5 * 1024 * 1024;
    private static final int LOG_ROTATION_COUNT = 3;
    private static final String LOG_PATTERN = "log_%g_" + FECHA + ".log";
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Logger.class.getName());
    private static Logger logger;

    private Logger() {
        try {
            UtilidadesFichero.createLogFolder();
            Path logDir = UtilidadesFichero.getLogDir();
            FileHandler fileHandler = new FileHandler(logDir.resolve(LOG_PATTERN).toString(), LOG_ROTATION_SIZE_BYTES, LOG_ROTATION_COUNT, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            LOG.addHandler(fileHandler);
            LOG.setUseParentHandlers(false);
            LOG.setLevel(Level.ALL);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Crear archivo logs", e);
        }
    }

    public static void init() {
        if (logger == null) {
            logger = new Logger();
        }
    }

    public static void eliminarLogsVacios() {
        UtilidadesFichero.createLogFolder();
        File carpeta = UtilidadesFichero.getLogDir().toFile();
        File[] lista = carpeta.listFiles();
        if (lista != null) {
            Arrays.stream(lista)
                    .filter(f -> f.isFile() && f.getName().endsWith(".log") && !f.getName().endsWith(FECHA + ".log"))
                    .forEach(Logger::eliminarLogsVacios);
        }
    }

    private static void eliminarLogsVacios(File file) {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String linea = bufferedReader.readLine();
            if (linea == null) {
                Files.delete(file.toPath());
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Lectura logs", e);
        }
    }

    public static void error(String mensaje, Exception e) {
        LOG.log(Level.SEVERE, mensaje, e);
    }

    public static void error(Exception e) {
        LOG.log(Level.SEVERE, null, e);
    }

    public static void info(String mensaje, Exception e) {
        LOG.log(Level.INFO, mensaje, e);
    }
}
