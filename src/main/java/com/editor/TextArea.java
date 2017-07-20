package com.editor;

import com.editor.model.TextEditorModel;
import com.editor.parser.SyntaxParser;

import javax.swing.*;

public class TextArea {
    public JScrollPane jScrollPane;
    public DrawComponent jComponent;

    private TextEditorModel model;
    public JFrame frame;

    public TextArea(JFrame frame) {
        this.frame = frame;
        model = new TextEditorModel();
        jComponent = new DrawComponent(model);
        jComponent.setActionMap(new TextActionMap(model, this));
        jComponent.setInputMap(JComponent.WHEN_FOCUSED, new TextInputMap());
        jScrollPane = new JScrollPane(jComponent);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public void render() {
        DrawComponent jComponent = this.jComponent;
        jComponent.repaint();
        jComponent.revalidate();
        jComponent.setScrollToCursorOnceOnPaint(true);
        if (!SyntaxParser.isTextSyntax()) {
            model.updatePairedBrackets();
        }
    }
}
