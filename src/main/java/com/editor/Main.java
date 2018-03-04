package com.editor;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Stream;

public class Main {
    /**
     * Main class for text-editor module
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        openApplication(args);
    }

    public static EditorFrame openApplication(String[] args) throws FileNotFoundException {
        EditorFrame frame = new EditorFrame();
        frame.setVisible(true);

        String fileArgPrefix = "--file:";
        String fileArg = Stream.of(args)
                .filter(i -> i.startsWith(fileArgPrefix))
                .findAny()
                .orElse(null);

        if (fileArg != null) {
            String fileName = fileArg.substring(fileArgPrefix.length());

            EventQueue.invokeLater(() -> {
                frame.getFileManager().openFile(new File(fileName));
                frame.renderTextArea();
            });
        }

        return frame;
    }
}
