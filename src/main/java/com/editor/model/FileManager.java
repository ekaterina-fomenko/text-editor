package com.editor.model;

import com.editor.TextArea;
import com.editor.parser.SyntaxParser;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public String fileName;
    public String directory;
    public JFrame frame;
    public TextEditorModel model;

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
                System.out.println("Cannot find  file " + fileName + " : " + e);
            } catch (IOException e) {
                System.out.println("Exception was occurred while trying to read file " + fileName + " from buffer: " + e);
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

            } catch (IOException e) {
                System.out.println("Exception was occurred while trying to write into file: " + fileName + ", exception: " + e);
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
        } catch (IOException e) {
            System.out.println("Exception was occurred while trying to write into file: " + fileName + ", exception: " + e);
        }
    }

    private void setTitleAndSyntax() {
        if (fileName != null) {
            frame.setTitle(fileName);
            int lastIndex = fileName.lastIndexOf(".");
            String fileExtension = fileName.substring(lastIndex + 1);
            SyntaxParser.setCurrentSyntaxByFileExtension(fileExtension);
        }
    }
}
