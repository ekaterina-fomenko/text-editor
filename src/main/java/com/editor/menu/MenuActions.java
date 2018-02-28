package com.editor.menu;

import com.editor.EditorSettings;
import com.editor.TextArea;
import com.editor.model.FileManager;
import com.editor.model.undo.UndoRedoService;
import com.editor.syntax.SyntaxType;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Process all actions from menu bar
 */

public class MenuActions extends AbstractAction {

    private final JFrame frame;
    public JComponent jComponent;
    public TextArea textArea;
    private EditorSettings editorSettings;
    public FileManager fileManager;
    private final UndoRedoService undoRedoService;

    public MenuActions(TextArea textArea, EditorSettings editorSettings) {
        this.textArea = textArea;
        this.editorSettings = editorSettings;
        this.jComponent = textArea.ropeDrawComponent;
        undoRedoService = textArea.undoRedoService;
        frame = textArea.frame;

        this.fileManager = new FileManager(
                textArea.frame,
                textArea.ropeModel,
                editorSettings);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JMenuItem event = (JMenuItem) e.getSource();
        event.setSelected(false);

        String filePath;

        switch (event.getText()) {
            case MenuBar.TEXT:
                editorSettings.setCurrentSyntax(SyntaxType.TEXT);
                break;
            case MenuBar.JAVASCRIPT:
                editorSettings.setCurrentSyntax(SyntaxType.JAVASCRIPT);
                break;
            case MenuBar.ERLANG:
                editorSettings.setCurrentSyntax(SyntaxType.ERLANG);
                break;
            case MenuBar.HASKELL:
                editorSettings.setCurrentSyntax(SyntaxType.HASKELL);
                break;
            case MenuBar.OPEN_FILE:
                filePath = fileManager.openFile();
                updateSyntaxAndFilePath(filePath);
                break;
            case MenuBar.SAVE_FILE:
                filePath = fileManager.saveFile();
                updateSyntaxAndFilePath(filePath);
                break;
            case MenuBar.SAVE_AS_FILE:
                filePath = fileManager.saveAsFile();
                updateSyntaxAndFilePath(filePath);
                break;
        }

        textArea.render();
    }

    private void updateSyntaxAndFilePath(String currentFilePath) {
        editorSettings.setCurrentFilePath(currentFilePath);

        if (currentFilePath != null) {
            int lastIndex = currentFilePath.lastIndexOf(".");
            String fileExtension = currentFilePath.substring(lastIndex + 1);
            editorSettings.setCurrentSyntax(SyntaxType.getByExtension(fileExtension));
            frame.setTitle(editorSettings.getFileName());
        }
    }
}
