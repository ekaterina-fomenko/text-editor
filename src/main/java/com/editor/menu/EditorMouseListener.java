package com.editor.menu;

import com.editor.parser.Syntax;
import com.editor.parser.SyntaxParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
//ToDo: remove this class
public class EditorMouseListener implements MouseListener{
    public static final Color BACKGROUND_COLOR = new Color(25, 51, 0);
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;


    private JComponent jComponent;
    private JMenu previousEvent;

    public EditorMouseListener(JComponent jComponent){
        this.jComponent = jComponent;
        previousEvent = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(previousEvent != null){
            previousEvent.setBackground(DEFAULT_BACKGROUND_COLOR);
        }
        JMenu event = (JMenu) e.getSource();
        previousEvent = event;
        event.setSelected(false);
        event.setBackground(BACKGROUND_COLOR);
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
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
