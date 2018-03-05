package com.editor;

import javax.swing.*;

public interface Renderer {
    void render();

    void render(boolean forceScrollToCursor);

    JFrame getFrame();
}
