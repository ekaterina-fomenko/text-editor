package com.editor.model;

import com.editor.model.undo.UndoRedoService;
import com.editor.syntax.SyntaxSetter;
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
    private String fileName;
    private String directory;
    private JFrame frame;
    private RopeTextEditorModel model;
    private UndoRedoService undoRedoService;

    public static Logger log = LoggerFactory.getLogger(FileManager.class);

    public FileManager(JFrame frame, RopeTextEditorModel model, UndoRedoService undoRedoService) {
        this.fileName = null;
        this.directory = null;
        this.frame = frame;
        this.model = model;
        this.undoRedoService = undoRedoService;
    }

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (directory != null) {
            fileChooser.setCurrentDirectory(new File(directory));
        }
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            openFile(file);
        }
    }

    public void openFile(File file) {
        long openStart = System.currentTimeMillis();

        model.reset();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            char[] buffer = new char[5000 * 1000];
            int countRead;

            while ((countRead = reader.read(buffer)) != -1) {
                char[] charsRead = countRead == buffer.length ? buffer : StringUtils.subArray(buffer, 0, countRead);
                model.append(charsRead);
            }

            fileName = file.getName();
            directory = file.getParentFile().getAbsolutePath();
            setTitleAndSyntax();

        } catch (FileNotFoundException e) {
            log.error("Cannot find  file {}", fileName, e);
        } catch (IOException e) {
            log.error("Exception was occurred while trying to read file {} from buffer", fileName, e);
        }
        undoRedoService.reset();
        long openEnd = System.currentTimeMillis();
        log.debug("File '{}' opened in {}ms", fileName, openEnd - openStart);
    }

    private char[] removeWindowsEndings(char[] charsRead) {
        char illegalSymbol = '\r';
        int counter = 0;
        for (char c : charsRead) {
            if (c == illegalSymbol) {
                counter++;
            }
        }

        char[] result = new char[charsRead.length - counter];

        counter = 0;
        for (char c : charsRead) {
            if (c != illegalSymbol) {
                result[counter] = c;
                counter++;
            }
        }

        return result;
    }

    public void saveAsFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                FileWriter writer = new FileWriter(chooser.getSelectedFile());
                writer.write(model.getRope().toString());
                writer.close();
                fileName = chooser.getName(chooser.getSelectedFile());
                directory = chooser.getCurrentDirectory().getPath();
                setTitleAndSyntax();
                log.info("File was saved successfully");
            } catch (IOException e) {
                log.error("Exception was occurred while trying to write into file {}", fileName, e);
            }
        }
    }

    public void saveFile() {
        if (fileName == null || directory == null) {
            saveAsFile();
            return;
        }
        try {
            FileWriter writer = new FileWriter(new File(directory + "/" + fileName));
            writer.write(model.getRope().toString());
            writer.close();
            log.info("File was saved successfully");
        } catch (IOException e) {
            log.error("Exception was occurred while trying to write into file {}", fileName, e);
        }
    }

    /**
     * Set file name as title to editor frame
     */
    private void setTitleAndSyntax() {
        if (fileName != null) {
            frame.setTitle(fileName);
            int lastIndex = fileName.lastIndexOf(".");
            String fileExtension = fileName.substring(lastIndex + 1);
            SyntaxSetter.setCurrentSyntaxByFileExtension(fileExtension);
        }
    }
}
