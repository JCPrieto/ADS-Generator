package auxiliar;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class IconUtils {
    private static final String ICONS_DIR = "img/icons/";
    private static final Map<String, ImageIcon> ICON_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Image> IMAGE_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, ImageIcon> SCALED_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, List<Image>> WINDOW_ICONS_CACHE = new ConcurrentHashMap<>();

    private IconUtils() {
    }

    public static ImageIcon loadIcon(String name) {
        return ICON_CACHE.computeIfAbsent(name, IconUtils::loadIconInternal);
    }

    public static Image loadImage(String name) {
        return IMAGE_CACHE.computeIfAbsent(name, IconUtils::loadImageInternal);
    }

    public static ImageIcon loadIconScaled(String name, int width, int height) {
        String key = name + ":" + width + "x" + height;
        return SCALED_CACHE.computeIfAbsent(key, k -> loadIconScaledInternal(name, width, height));
    }

    public static List<Image> loadWindowIcons(String name) {
        return WINDOW_ICONS_CACHE.computeIfAbsent(name, IconUtils::loadWindowIconsInternal);
    }

    private static ImageIcon loadIconInternal(String name) {
        URL url = IconUtils.class.getClassLoader().getResource(ICONS_DIR + name);
        if (url == null) {
            return null;
        }
        return new ImageIcon(url);
    }

    private static Image loadImageInternal(String name) {
        ImageIcon icon = loadIcon(name);
        if (icon == null) {
            return null;
        }
        return icon.getImage();
    }

    private static ImageIcon loadIconScaledInternal(String name, int width, int height) {
        Image image = loadImage(name);
        if (image == null) {
            return null;
        }
        return new ImageIcon(scaleImage(image, width, height));
    }

    private static List<Image> loadWindowIconsInternal(String name) {
        Image image = loadImage(name);
        if (image == null) {
            return List.of();
        }

        int[] sizes = {16, 24, 32, 48, 64, 128};
        List<Image> icons = new ArrayList<>(sizes.length);
        for (int size : sizes) {
            icons.add(scaleImage(image, size, size));
        }
        return icons;
    }

    private static BufferedImage scaleImage(Image image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaledImage.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage(image, 0, 0, width, height, null);
        } finally {
            graphics.dispose();
        }
        return scaledImage;
    }

    public static void clearCaches() {
        ICON_CACHE.clear();
        IMAGE_CACHE.clear();
        SCALED_CACHE.clear();
        WINDOW_ICONS_CACHE.clear();
    }
}
