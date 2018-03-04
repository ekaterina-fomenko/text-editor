package com.editor;

import com.editor.syntax.SyntaxType;

import java.io.File;

/**
 * Current user settings - what user choose in menu
 */
public class EditorSettings {
    private SyntaxType currentSyntax = SyntaxType.TEXT;
    private String currentFilePath;

    public SyntaxType getCurrentSyntax() {
        return currentSyntax;
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public void setCurrentSyntax(SyntaxType currentSyntax) {
        this.currentSyntax = currentSyntax;
    }

    public void setCurrentFilePath(String path) {
        this.currentFilePath = path;
    }

    public String getFileName() {
        if (currentFilePath == null) {
            return null;
        }

        return new File(currentFilePath).getName();
    }
}
