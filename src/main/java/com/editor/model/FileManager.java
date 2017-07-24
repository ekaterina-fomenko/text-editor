package com.editor.model;

import com.editor.TextArea;

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
                frame.setTitle(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
