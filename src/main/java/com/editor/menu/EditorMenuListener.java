package com.editor.menu;

import com.editor.parser.Syntax;
import com.editor.parser.SyntaxParser;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class EditorMenuListener implements MenuListener {
    private JComponent jComponent;

    public EditorMenuListener(JComponent jComponent) {
        this.jComponent = jComponent;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        JMenu event = (JMenu) e.getSource();
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

    @Override
    public void menuDeselected(MenuEvent e) {
    }

    @Override
    public void menuCanceled(MenuEvent e) {
    }
}
