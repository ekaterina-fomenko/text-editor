package com.editor.model;

import com.editor.EditorSettings;
import com.editor.model.undo.UndoRedoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileManagerTest {

    public static final String FILES_PATH = "src/test/resources/";

    RopeTextEditorModel model;

    FileManager fileManager;

    @Mock
    JFrame frame;

    @Mock
    EditorSettings editorSettings;

    @Mock
    UndoRedoService undoRedoService;

    @Before
    public void setUp() {
        model = new RopeTextEditorModel();
        fileManager = new FileManagerImpl(frame, model, editorSettings, undoRedoService);
    }

    @Test
    public void testOpenFile() throws IOException {
        model.append("Hello".toCharArray());
        String filePath = FILES_PATH + "test.txt";
        File file = new File(filePath);

        String resultAbsolutePath = fileManager.openFile(file);

        assertEquals(file.getAbsolutePath(), resultAbsolutePath);

        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        String expectedText = new String(bytes, Charset.defaultCharset());

        assertEquals(expectedText, model.getRope().toString());
        verify(undoRedoService, times(1)).reset();
    }

    @Test
    public void testSaveFile() throws IOException {
        String filePath = FILES_PATH + "testSave.txt";
        when(editorSettings.getCurrentFilePath()).thenReturn(filePath);
        String expectedText = "I want to save this...";
        model.append(expectedText.toCharArray());
        fileManager.saveFile();

        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        String resultText = new String(bytes, Charset.defaultCharset());
        assertEquals(expectedText, resultText);

    }

}
