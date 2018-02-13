package com.editor.menu;

import com.editor.TextArea;
import com.editor.model.FileManager;
import com.editor.parser.Syntax;
import com.editor.parser.SyntaxParser;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Process all actions which made in menu bar
 */

public class MenuActions extends AbstractAction {

    public JComponent jComponent;
    public TextArea textArea;
    public FileManager fileManager;

    public MenuActions(TextArea textArea) {

        this.textArea = textArea;
        this.jComponent = textArea.ropeDrawComponent;
        this.fileManager = new FileManager(textArea);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JMenuItem event = (JMenuItem) e.getSource();
        event.setSelected(false);

        switch (event.getText()) {
            case MenuBar.TEXT:
                SyntaxParser.setCurrentSyntax(Syntax.TEXT);
                break;
            case MenuBar.JAVASCRIPT:
                SyntaxParser.setCurrentSyntax(Syntax.JAVASCRIPT);
                break;
            case MenuBar.ERLANG:
                SyntaxParser.setCurrentSyntax(Syntax.ERLANG);
                break;
            case MenuBar.HASKELL:
                SyntaxParser.setCurrentSyntax(Syntax.HASKELL);
                break;
            case MenuBar.OPEN_FILE:
                //Todo: open file extension is broken - if open js and then java - syntax still js
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
