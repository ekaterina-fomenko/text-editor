package com.editor;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Stream;

/**
 * Main class for text-editor module
 *
 */
public class Main {

    public static final String FILE_PREFIX = "--file:";

    public static void main(String[] args) throws Exception {
        openApplication(args);
    }

    public static EditorFrame openApplication(String[] args) throws FileNotFoundException {
        EditorFrame frame = new EditorFrame();
        frame.setVisible(true);

        String fileName = tryFetchFileArg(args);

        if (fileName != null) {
            EventQueue.invokeLater(() -> {
                frame.getFileManager().openFile(new File(fileName));
                frame.renderTextArea();
            });
        }

        return frame;
    }

    static String tryFetchFileArg(String[] args) {
        String fileArg = Stream.of(args)
                .filter(i -> i.startsWith(FILE_PREFIX))
                .findAny()
                .orElse(null);

        if (fileArg != null) {
            return fileArg.substring(FILE_PREFIX.length());
        }
        return null;
    }
}
