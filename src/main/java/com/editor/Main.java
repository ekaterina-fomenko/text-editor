package com.editor;

import java.io.File;
import java.util.stream.Stream;

public class Main {
    /**
     * Main class for text-editor module
     *
     * @param args
     */
    public static void main(String[] args) {
        openApplication(args);
    }

    public static EditorFrame openApplication(String[] args) {
        EditorFrame frame = new EditorFrame();
        frame.setVisible(true);
        String fileArgPrefix = "--file:";

        Stream.of(args)
                .filter(i -> i.startsWith(fileArgPrefix))
                .findAny()
                .ifPresent(i -> {
                    String fileName = i.substring(fileArgPrefix.length());

                    frame.menuBar.getMenuActions().fileManager.openFile(new File(fileName));
                });

        return frame;
    }
}
