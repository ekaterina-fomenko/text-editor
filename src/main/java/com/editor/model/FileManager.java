package com.editor.model;

import java.io.File;

public interface FileManager {
    String openFile();

    String openFile(File file);

    String saveAsFile();

    String saveFile();
}
