package com.editor.model;

import com.editor.EditorSettings;
import com.editor.model.undo.UndoRedoService;
import com.editor.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;

/**
 * This class contains all info about file.
 * And also helps to process such actions with file as: save as file, save updates in file, open file
 */

public class FileManager {
    private JFrame frame;
    private RopeTextEditorModel model;
    private EditorSettings editorSettings;
    private UndoRedoService undoRedoService;

    public static Logger log = LoggerFactory.getLogger(FileManager.class);

    public FileManager(JFrame frame, RopeTextEditorModel model, EditorSettings editorSettings) {
        this.frame = frame;
        this.model = model;
        this.editorSettings = editorSettings;
    }

    public String openFile() {
        JFileChooser fileChooser = new JFileChooser();
        String currentFile = editorSettings.getCurrentFilePath();
        if (currentFile != null) {
            fileChooser.setCurrentDirectory(new File(currentFile).getParentFile());
        }

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return openFile(file);
        } else {
            return null;
        }
    }

    public String openFile(File file) {
        long openStart = System.currentTimeMillis();
        String resultPath = null;

        model.reset();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            char[] buffer = new char[50 * 1000 * 1000];
            int countRead;

            while ((countRead = reader.read(buffer)) != -1) {
                char[] charsRead = countRead == buffer.length ? buffer : StringUtils.subArray(buffer, 0, countRead);
                model.append(charsRead);
            }

            resultPath = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            log.error("Cannot find  file {}", file.getAbsolutePath(), e);
        } catch (IOException e) {
            log.error("Exception was occurred while trying to read file {} from buffer", file.getAbsolutePath(), e);
        }

        long openEnd = System.currentTimeMillis();
        log.debug("File '{}' opened in {}ms", file.getName(), openEnd - openStart);

        return resultPath;
    }

    public String saveAsFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                FileWriter writer = new FileWriter(chooser.getSelectedFile());
                writer.write(model.getRope().toString());
                writer.close();

                log.info("File was saved successfully");

                return chooser.getSelectedFile().getAbsolutePath();
            } catch (IOException e) {
                log.error("Exception was occurred while trying to write into file {}", e);
                return null;
            }
        } else {
            return null;
        }
    }

    public String saveFile() {
        if (editorSettings.getCurrentFilePath() == null) {
            return saveAsFile();
        }

        try {
            FileWriter writer = new FileWriter(new File(editorSettings.getCurrentFilePath()));
            writer.write(model.getRope().toString());
            writer.close();
            log.info("File was saved successfully");
        } catch (IOException e) {
            log.error("Exception was occurred while trying to write into file {}", e);
            return null;
        }

        return editorSettings.getCurrentFilePath();
    }
}
