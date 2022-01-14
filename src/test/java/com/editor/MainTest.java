package com.editor;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void tryFetFileArg() throws Exception {
        assertEquals("filePath", Main.tryFetchFileArg(new String[]{"--file:filePath"}));
    }
}