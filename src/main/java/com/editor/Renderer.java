package com.editor;

import com.sun.deploy.uitoolkit.ui.ConsoleWindow;

import javax.swing.*;

public interface Renderer {
    void render();

    void render(boolean forceScrollToCursor);

    JFrame getFrame();
}
