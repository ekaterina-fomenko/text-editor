package com.editor.menu;

import com.editor.TextArea;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Contains all menu items which user see in menu bar
 */

public class MenuBar {

    private static final String SYNTAX_MENU = "Syntax";
    private static final String FILE_MENU = "File";

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

        syntaxMenu = new JMenu(SYNTAX_MENU);
        fileMenu = new JMenu(FILE_MENU);

        createSyntaxMenuItems();
        createFileMenuItems();

        addListenerToMenuItems();

        setHotKeysToFileItems();

        addItemsToSyntaxMenu();
        addItemsToFileMenu();

        menuBar.add(syntaxMenu);
        menuBar.add(fileMenu);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private void setHotKeysToFileItems() {
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        saveAsFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
    }

    private void createSyntaxMenuItems() {
        plainTextSyntax = new JMenuItem(TEXT);
        javaScriptSyntax = new JMenuItem(JAVASCRIPT);
        haskellSyntax = new JMenuItem(HASKELL);
        erlangSyntax = new JMenuItem(ERLANG);
    }

    private void createFileMenuItems() {
        openFile = new JMenuItem(OPEN_FILE);
        saveFile = new JMenuItem(SAVE_FILE);
        saveAsFile = new JMenuItem(SAVE_AS_FILE);
    }

    private void addItemsToFileMenu() {
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
    }

    private void addItemsToSyntaxMenu() {
        syntaxMenu.add(haskellSyntax);
        syntaxMenu.add(plainTextSyntax);
        syntaxMenu.add(javaScriptSyntax);
        syntaxMenu.add(erlangSyntax);
    }

    private void addListenerToMenuItems() {
        plainTextSyntax.addActionListener(menuListener);
        javaScriptSyntax.addActionListener(menuListener);
        erlangSyntax.addActionListener(menuListener);
        haskellSyntax.addActionListener(menuListener);

        openFile.addActionListener(menuListener);
        saveFile.addActionListener(menuListener);
        saveAsFile.addActionListener(menuListener);
    }
}
