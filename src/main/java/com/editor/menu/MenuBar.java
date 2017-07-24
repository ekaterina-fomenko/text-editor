package com.editor.menu;

import com.editor.TextArea;

import javax.swing.*;

public class MenuBar {
    public static final String TEXT = "Text";
    public static final String JAVASCRIPT = "JavaScript";
    public static final String ERLANG = "Erlang";
    public static final String HASKELL = "Haskell";

    public static final String OPEN_FILE = "Open";
    public static final String SAVE_FILE = "Save";
    public static final String SAVE_AS_FILE = "Save As...";


    private JMenuBar menuBar;

    private JMenu syntaxMenu;

    private JMenuItem plainTextSyntax;
    private JMenuItem javaScriptSyntax;
    private JMenuItem erlangSyntax;
    private JMenuItem haskellSyntax;

    private JMenu fileMenu;

    private JMenuItem openFile;
    private JMenuItem saveFile;
    private JMenuItem saveAsFile;

    private MenuActions menuListener;

    public MenuBar(TextArea textArea) {
        menuListener = new MenuActions(textArea);
        menuBar = new JMenuBar();

        syntaxMenu = new JMenu("Syntax");
        fileMenu = new JMenu("File");

        plainTextSyntax = new JMenuItem(TEXT);
        javaScriptSyntax = new JMenuItem(JAVASCRIPT);
        haskellSyntax = new JMenuItem(HASKELL);
        erlangSyntax = new JMenuItem(ERLANG);

        openFile = new JMenuItem(OPEN_FILE);
        saveFile = new JMenuItem(SAVE_FILE);
        saveAsFile = new JMenuItem(SAVE_AS_FILE);

        plainTextSyntax.addActionListener(menuListener);
        javaScriptSyntax.addActionListener(menuListener);
        erlangSyntax.addActionListener(menuListener);
        haskellSyntax.addActionListener(menuListener);

        openFile.addActionListener(menuListener);
        saveFile.addActionListener(menuListener);
        saveAsFile.addActionListener(menuListener);

        syntaxMenu.add(haskellSyntax);
        syntaxMenu.add(plainTextSyntax);
        syntaxMenu.add(javaScriptSyntax);
        syntaxMenu.add(erlangSyntax);

        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);

        menuBar.add(syntaxMenu);
        menuBar.add(fileMenu);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
