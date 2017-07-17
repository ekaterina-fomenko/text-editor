package com.editor;

import com.editor.model.TextEditorModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextArea implements ActionListener {
    public static final String SYNTAX_MENU = "Syntax";
    public static final String JAVASCRIPT_ITEM = "JavaScript";
    public static final String ERLANG_ITEM = "Erlang";
    public static final String HASKELL_ITEM = "Haskell";

    public JMenuBar menuBar;
    public JMenu syntax;
    public JMenuItem javaScript;
    public JMenuItem erlang;
    public JMenuItem haskell;
    public JScrollPane jScrollPane;
    public JComponent jComponent;
    public JScrollBar hbar;
    public JScrollBar vbar;


    private TextEditorModel model;

    public TextArea() {
        hbar=new JScrollBar(JScrollBar.HORIZONTAL, 30, 20, 0, 500);
        vbar=new JScrollBar(JScrollBar.VERTICAL, 30, 40, 0, 500);

        model = new TextEditorModel();
        jComponent = new DrawComponent(model);
        jComponent.setActionMap(new TextActionMap(model, this));
        jComponent.setInputMap(JComponent.WHEN_FOCUSED, new TextInputMap());
        jScrollPane = new JScrollPane(jComponent);

        menuBar = new JMenuBar();
        syntax = new JMenu(SYNTAX_MENU);
        javaScript = new JMenuItem(JAVASCRIPT_ITEM);
        erlang = new JMenuItem(ERLANG_ITEM);
        haskell = new JMenuItem(HASKELL_ITEM);

        syntax.add(javaScript);
        syntax.add(erlang);
        syntax.add(haskell);
        menuBar.add(syntax);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JMenuItem item = (JMenuItem) event.getSource();
        switch (item.getText()) {
            case JAVASCRIPT_ITEM:
            case ERLANG_ITEM:
            case HASKELL_ITEM:
        }
    }
}
