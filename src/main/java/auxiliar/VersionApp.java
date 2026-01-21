package auxiliar;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class VersionApp {
    private static final String VERSION_FALLBACK = "1.0";

    private VersionApp() {
    }

    public static String getVersion() {
        String version = leerVersionPomProperties();
        if (version != null && !version.isEmpty()) {
            return version;
        }

        version = leerVersionPom();
        if (version != null && !version.isEmpty()) {
            return version;
        }

        return VERSION_FALLBACK;
    }

    private static String leerVersionPomProperties() {
        String resourcePath = "/META-INF/maven/es.jcprieto/ads-generator/pom.properties";
        try (InputStream in = VersionApp.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                return null;
            }
            Properties props = new Properties();
            props.load(in);
            return props.getProperty("version");
        } catch (Exception e) {
            return null;
        }
    }

    private static String leerVersionPom() {
        InputStream in = VersionApp.class.getResourceAsStream("/pom.xml");
        try {
            if (in == null) {
                Path pomPath = Path.of("pom.xml");
                if (Files.exists(pomPath)) {
                    in = Files.newInputStream(pomPath);
                }
            }

            if (in == null) {
                return null;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().parse(in);
            XPath xpath = XPathFactory.newInstance().newXPath();
            String version = xpath.evaluate("/*[local-name()='project']/*[local-name()='version']/text()", doc);
            return version == null ? null : version.trim();
        } catch (Exception e) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    // Ignore close errors.
                }
            }
        }
    }
}
