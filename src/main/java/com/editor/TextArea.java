package main.java.com.editor;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextArea implements ActionListener {
    public static final String SYNTAX_MENU = "Syntax";
    public static final String JAVASCRIPT_ITEM = "JavaScript";
    public static final String ERLANG_ITEM = "Erlang";
    public static final String HASKELL_ITEM = "Haskell";

    public JTextArea jTextArea;
    public JMenuBar menuBar;
    public JMenu syntax;
    public JMenuItem javaScript;
    public JMenuItem erlang;
    public JMenuItem haskell;
    public JScrollPane jScrollPane;

    public TextArea() {
        jTextArea = new JTextArea();
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setBackground(new Color(99, 74, 68));
        jTextArea.setForeground(Color.WHITE);
        jScrollPane = new JScrollPane(jTextArea);

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
    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();
        switch (item.getText()){
            case JAVASCRIPT_ITEM:
            case ERLANG_ITEM:
            case HASKELL_ITEM:
        }
    }
}
