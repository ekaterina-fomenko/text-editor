package com.editor.menu;

import com.editor.*;
import com.editor.Renderer;
import com.editor.model.FileManager;
import com.editor.model.FileManagerImpl;
import com.editor.model.undo.UndoRedoService;
import com.editor.syntax.SyntaxType;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Process all actions from menu bar
 */

public class MenuActions extends AbstractAction {
    private final Renderer renderer;
    private final EditorSettings editorSettings;
    private final FileManager fileManager;
    private final UndoRedoService undoRedoService;

    public MenuActions(Renderer renderer,
                       EditorSettings editorSettings,
                       FileManagerImpl fileManager,
                       UndoRedoService undoRedoService) {
        this.renderer = renderer;
        this.editorSettings = editorSettings;
        this.undoRedoService = undoRedoService;

        this.fileManager = fileManager;
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

        renderer.render();
    }

    private void updateSyntaxAndFilePath(String currentFilePath) {
        editorSettings.setCurrentFilePath(currentFilePath);

        if (currentFilePath != null) {
            int lastIndex = currentFilePath.lastIndexOf(".");
            String fileExtension = currentFilePath.substring(lastIndex + 1);
            editorSettings.setCurrentSyntax(SyntaxType.getByExtension(fileExtension));
            renderer.getFrame().setTitle(editorSettings.getFileName());
        }
    }
}
