package com.editor;

import com.editor.model.TextEditorModel;

import javax.swing.*;

public class TextArea {
    public JScrollPane jScrollPane;
    public JComponent jComponent;
    public JScrollBar hbar;
    public JScrollBar vbar;


    private TextEditorModel model;

    public TextArea() {
        hbar = new JScrollBar(JScrollBar.HORIZONTAL, 30, 20, 0, 500);
        vbar = new JScrollBar(JScrollBar.VERTICAL, 30, 40, 0, 500);

        model = new TextEditorModel();
        jComponent = new DrawComponent(model);
        jComponent.setActionMap(new TextActionMap(model, this));
        jComponent.setInputMap(JComponent.WHEN_FOCUSED, new TextInputMap());
        jScrollPane = new JScrollPane(jComponent);

    }
}
