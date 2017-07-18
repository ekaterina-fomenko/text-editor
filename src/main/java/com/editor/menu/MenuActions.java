package com.editor.menu;

import com.editor.parser.Syntax;
import com.editor.parser.SyntaxParser;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuActions extends AbstractAction {
    public JComponent jComponent;

    public MenuActions(JComponent jComponent){
        this.jComponent = jComponent;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem event = (JMenuItem) e.getSource();
        event.setSelected(false);
        switch (event.getText()) {
            case MenuBar.TEXT:
                SyntaxParser.setSyntax(Syntax.TEXT);
                break;
            case MenuBar.JAVASCRIPT:
                SyntaxParser.setSyntax(Syntax.JAVASCRIPT);
                break;
            case MenuBar.ERLANG:
                SyntaxParser.setSyntax(Syntax.ERLANG);
                break;
            case MenuBar.HASKELL:
                SyntaxParser.setSyntax(Syntax.HASKELL);
                break;
        }
        jComponent.repaint();
    }
}
