package com.editor.menu;

import com.editor.TextArea;
import com.editor.model.FileManager;
import com.editor.parser.SyntaxParser;
import com.editor.parser.SyntaxType;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Process all actions from menu bar
 */

public class MenuActions extends AbstractAction {

    public JComponent jComponent;
    public TextArea textArea;
    public FileManager fileManager;

    public MenuActions(TextArea textArea) {

        this.textArea = textArea;
        this.jComponent = textArea.ropeDrawComponent;
        this.fileManager = new FileManager(textArea.frame, textArea.ropeModel, textArea.undoRedoService);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JMenuItem event = (JMenuItem) e.getSource();
        event.setSelected(false);

        switch (event.getText()) {
            case MenuBar.TEXT:
                SyntaxParser.setCurrentSyntax(SyntaxType.TEXT);
                break;
            case MenuBar.JAVASCRIPT:
                SyntaxParser.setCurrentSyntax(SyntaxType.JAVASCRIPT);
                break;
            case MenuBar.ERLANG:
                SyntaxParser.setCurrentSyntax(SyntaxType.ERLANG);
                break;
            case MenuBar.HASKELL:
                SyntaxParser.setCurrentSyntax(SyntaxType.HASKELL);
                break;
            case MenuBar.OPEN_FILE:
                fileManager.openFile();
                break;
            case MenuBar.SAVE_FILE:
                fileManager.saveFile();
                break;
            case MenuBar.SAVE_AS_FILE:
                fileManager.saveAsFile();
        }

        textArea.render();
    }
}
