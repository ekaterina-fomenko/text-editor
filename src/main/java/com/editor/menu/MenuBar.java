package com.editor.menu;

import javax.swing.*;

public class MenuBar {
    public static final String TEXT = "Text";
    public static final String JAVASCRIPT = "JavaScript";
    public static final String ERLANG = "Erlang";
    public static final String HASKELL = "Haskell";

    private JMenuBar menuBar;

    private JMenu plainTextSyntax;

    private JMenu javaScriptSyntax;
    private JMenu erlangSyntax;
    private JMenu haskellSyntax;
    private EditorMenuListener menuListener;
    public MenuBar(JComponent jComponent){
        menuListener = new EditorMenuListener(jComponent);
        menuBar = new JMenuBar();
        plainTextSyntax = new JMenu(TEXT);
        javaScriptSyntax = new JMenu(JAVASCRIPT);
        haskellSyntax = new JMenu(HASKELL) ;
        erlangSyntax = new JMenu(ERLANG);
        plainTextSyntax.addMenuListener(menuListener);
        javaScriptSyntax.addMenuListener(menuListener);
        erlangSyntax.addMenuListener(menuListener);
        haskellSyntax.addMenuListener(menuListener);

        menuBar.add(plainTextSyntax);
        menuBar.add(javaScriptSyntax);
        menuBar.add(haskellSyntax);
        menuBar.add(erlangSyntax);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
