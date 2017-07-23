package com.editor.menu;

import com.editor.TextArea;
import com.editor.parser.Syntax;
import com.editor.parser.SyntaxParser;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuActions extends AbstractAction {
    public JComponent jComponent;
    public TextArea textArea;

    public MenuActions(TextArea textArea){
        this.textArea = textArea;
        this.jComponent = textArea.jComponent;
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
        }
        textArea.render(true);
    }
}
