package auxiliar;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ADSFilter extends FileFilter {
    public boolean accept(File f) {
        boolean b;
        if (f.isDirectory()) {
            b = true;
        } else {
            String s = f.getName();
            if (s.endsWith("ads")) {
                b = true;
            } else {
                b = false;
            }
        }

        return b;
    }

    public String getDescription() {
        return "*.ads";
    }
}
