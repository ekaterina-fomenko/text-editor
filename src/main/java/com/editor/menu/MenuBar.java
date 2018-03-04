package com.editor.menu;

import com.editor.EditorSettings;
import com.editor.TextArea;
import com.editor.model.FileManagerImpl;
import com.editor.model.undo.UndoRedoService;

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

    private MenuActions menuActions;

    public MenuBar(TextArea textArea, EditorSettings editorSettings, FileManagerImpl fileManager, UndoRedoService undoRedoService) {
        menuActions = new MenuActions(textArea, editorSettings, fileManager, undoRedoService);
        menuBar = new JMenuBar();

        syntaxMenu = new JMenu(SYNTAX_MENU);
        fileMenu = new JMenu(FILE_MENU);

        createSyntaxMenuItems();
        createFileMenuItems();

        addListenerToMenuItems();

        setHotKeysToFileItems();

        addItemsToFileMenu();
        addItemsToSyntaxMenu();

        menuBar.add(fileMenu);
        menuBar.add(syntaxMenu);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public MenuActions getMenuActions() {
        return menuActions;
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
        syntaxMenu.add(plainTextSyntax);
        syntaxMenu.add(javaScriptSyntax);
        syntaxMenu.add(haskellSyntax);
        syntaxMenu.add(erlangSyntax);
    }

    private void addListenerToMenuItems() {
        plainTextSyntax.addActionListener(menuActions);
        javaScriptSyntax.addActionListener(menuActions);
        erlangSyntax.addActionListener(menuActions);
        haskellSyntax.addActionListener(menuActions);

        openFile.addActionListener(menuActions);
        saveFile.addActionListener(menuActions);
        saveAsFile.addActionListener(menuActions);
    }
}
