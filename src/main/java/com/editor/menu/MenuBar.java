package com.editor.menu;

import javax.swing.*;
//ToDo: remove comments
public class MenuBar {
    public static final String TEXT = "Text";
    public static final String JAVASCRIPT = "JavaScript";
    public static final String ERLANG = "Erlang";
    public static final String HASKELL = "Haskell";

    private JMenuBar menuBar;
    private JMenu menu;

    private JMenuItem plainTextSyntax;

    private JMenuItem javaScriptSyntax;
    private JMenuItem erlangSyntax;
    private JMenuItem haskellSyntax;
    //private EditorMouseListener menuListener;
    private MenuActions menuListener;

    public MenuBar(JComponent jComponent){
        menu = new JMenu("Syntax");
       //menuListener = new EditorMouseListener(jComponent);
        menuListener = new MenuActions(jComponent);
        menuBar = new JMenuBar();
        plainTextSyntax = new JMenuItem(TEXT);
        javaScriptSyntax = new JMenuItem(JAVASCRIPT);
        haskellSyntax = new JMenuItem(HASKELL) ;
        erlangSyntax = new JMenuItem(ERLANG);
        /* plainTextSyntax.addMouseListener(menuListener);
        javaScriptSyntax.addMouseListener(menuListener);
        erlangSyntax.addMouseListener(menuListener);
        haskellSyntax.addMouseListener(menuListener);*/
        plainTextSyntax.addActionListener(menuListener);
        javaScriptSyntax.addActionListener(menuListener);
        erlangSyntax.addActionListener(menuListener);
        haskellSyntax.addActionListener(menuListener);

        menu.add(haskellSyntax);
        menu.add(plainTextSyntax);
        menu.add(javaScriptSyntax);
        menu.add(erlangSyntax);
        menuBar.add(menu);
        /*menuBar.add(haskellSyntax);
        menuBar.add(plainTextSyntax);
        menuBar.add(javaScriptSyntax);
        menuBar.add(erlangSyntax);*/
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
