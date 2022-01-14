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

public class FileManagerImpl implements FileManager {
    private final JFrame frame;
    private final RopeTextEditorModel model;
    private final EditorSettings editorSettings;
    private final UndoRedoService undoRedoService;

    public static Logger log = LoggerFactory.getLogger(FileManagerImpl.class);

    public FileManagerImpl(JFrame frame, RopeTextEditorModel model, EditorSettings editorSettings, UndoRedoService undoRedoService) {
        this.frame = frame;
        this.model = model;
        this.editorSettings = editorSettings;
        this.undoRedoService = undoRedoService;
    }

    @Override
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

    @Override
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

        undoRedoService.reset();

        long openEnd = System.currentTimeMillis();
        log.debug("File '{}' opened in {}ms", file.getName(), openEnd - openStart);

        return resultPath;
    }

    @Override
    public String saveAsFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                writeToFile(chooser.getSelectedFile());
                return chooser.getSelectedFile().getAbsolutePath();
            } catch (IOException e) {
                log.error("Exception was occurred while trying to write into file", e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String saveFile() {
        if (editorSettings.getCurrentFilePath() == null) {
            return saveAsFile();
        }

        try {
            writeToFile(new File(editorSettings.getCurrentFilePath()));
        } catch (IOException e) {
            log.error("Exception was occurred while trying to write into file", e);
            return null;
        }

        return editorSettings.getCurrentFilePath();
    }

    private void writeToFile(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        model.getRope().writeTo(writer);
        writer.close();
        log.info("File was saved successfully");
    }
}
