package com.editor.model;

import com.editor.TextArea;
import com.editor.parser.SyntaxParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all info about file.
 * And also helps to process such actions with file as: save as file, save updates in file, open file
 */

public class FileManager {
    private String fileName;
    private String directory;
    private JFrame frame;
    private TextEditorModel model;

    public static Logger log = LoggerFactory.getLogger(FileManager.class);

    public FileManager(TextArea textArea) {
        this.fileName = null;
        this.directory = null;
        this.frame = textArea.frame;
        this.model = textArea.model;
    }

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (directory != null) {
            fileChooser.setCurrentDirectory(new File(directory));
        }
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String line;
            List<StringBuilder> text = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine()) != null) {
                    text.add(new StringBuilder(line));
                }
                model.setLineBuildersFromFile(text);
                fileName = fileChooser.getName(file);
                directory = fileChooser.getCurrentDirectory().getPath();
                setTitleAndSyntax();

            } catch (FileNotFoundException e) {
                log.error("Cannot find  file {}", fileName, e);
            } catch (IOException e) {
                log.error("Exception was occurred while trying to read file {} from buffer", fileName, e);
            }
        }
    }

    public void saveAsFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                FileWriter writer = new FileWriter(chooser.getSelectedFile());
                writer.write(model.lineBuildersToString());
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
        }
        try {
            FileWriter writer = new FileWriter(new File(directory + "/" + fileName));
            writer.write(model.lineBuildersToString());
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
            SyntaxParser.setCurrentSyntaxByFileExtension(fileExtension);
        }
    }
}