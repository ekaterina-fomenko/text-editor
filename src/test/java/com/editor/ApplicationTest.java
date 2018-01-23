package com.editor;

import com.editor.model.rope.CountingStringSizeProvider;
import com.editor.model.rope.RopeNode;
import com.editor.model.rope.StringSizeProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.PrintWriter;

import static org.junit.Assert.*;

@E2E
public class ApplicationTest {
    private StringSizeProvider originalSizeProvider;

    @Before
    public void before() {
        originalSizeProvider = RopeNode.getSizeProvider();
    }

    @After
    public void after() {
        RopeNode.setSizeProvider(originalSizeProvider);
    }

    @Test
    public void passingFileArgToMainShouldOpenFileImmediately() throws Exception {
        File tempFile = File.createTempFile("tempFile", "TextEditor");
        PrintWriter printWriter = new PrintWriter(tempFile);
        printWriter.append("text text text");
        printWriter.close();
        EditorFrame frame = Main.openApplication(new String[]{"--file:" + tempFile.getAbsolutePath()});

        EventQueue.invokeLater(() -> {
            assertEquals("text text text", frame.textArea.ropeModel.getRope().toString());
        });
    }
}