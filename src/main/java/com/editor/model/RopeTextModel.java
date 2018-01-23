package com.editor.model;

import com.editor.model.rope.Rope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RopeTextModel {
    private Rope rope = new Rope();

    public void addText(char[] text) {
        rope.append(text);
    }
}
