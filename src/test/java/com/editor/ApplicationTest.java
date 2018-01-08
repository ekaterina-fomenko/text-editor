package com.editor;

import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;

import static org.junit.Assert.*;

@E2E
public class ApplicationTest {
    @Test
    public void passingFileArgToMainShouldOpenFileImmediately() throws Exception {
        File tempFile = File.createTempFile("tempFile", "TextEditor");
        PrintWriter printWriter = new PrintWriter(tempFile);
        printWriter.append("text text text");
        printWriter.close();
        EditorFrame frame = Main.openApplication(new String[]{"--file:" + tempFile.getAbsolutePath()});

        assertEquals("text text text", frame.textArea.ropeModel.getRope().toString());
    }
}