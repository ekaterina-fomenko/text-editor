package com.editor.model;

import com.editor.model.rope.Rope;

public class RopeTextModel {
    private Rope rope = new Rope("");

    public void addText(String text) {
        rope.append(text);
    }
}
