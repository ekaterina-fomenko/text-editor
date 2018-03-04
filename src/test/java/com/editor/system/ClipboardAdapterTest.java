package com.editor.system;

import org.junit.Test;

import java.awt.datatransfer.DataFlavor;
import java.util.Optional;

import static org.junit.Assert.*;

public class ClipboardAdapterTest {

    @Test
    public void getText() throws Exception {
        assertEquals(
                "text",
                new ClipboardAdapter(new MockClipboardSystemApi(DataFlavor.stringFlavor, "text")).getText().get()
        );

        assertEquals(
                Optional.empty(),
                new ClipboardAdapter(new MockClipboardSystemApi(DataFlavor.imageFlavor, null)).getText()
        );

        assertEquals(
                Optional.empty(),
                new ClipboardAdapter(new MockClipboardSystemApi(DataFlavor.stringFlavor, null)).getText()
        );
    }

    @Test
    public void getTextThrowing() {
        Optional<String> result = new ClipboardAdapter(new MockClipboardSystemApi(DataFlavor.stringFlavor, "result") {
            @Override
            public Object getData(DataFlavor dataFlavor) throws Exception {
                throw new Exception();
            }
        }).getText();

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSetText() {
        ClipboardAdapter clipboardAdapter = new ClipboardAdapter(new MockClipboardSystemApi(DataFlavor.stringFlavor, "result"));

        clipboardAdapter.setText("newText");

        assertEquals("newText", clipboardAdapter.getText().get());
    }

    static class MockClipboardSystemApi implements ClipboardSystemApi {
        private DataFlavor flavor;
        private String text;

        public MockClipboardSystemApi(DataFlavor flavor, String text) {
            this.flavor = flavor;
            this.text = text;
        }

        @Override
        public boolean isDataFlavorAvailable(DataFlavor dataFlavor) {
            return this.flavor == dataFlavor;
        }

        @Override
        public void setContents(String text) {
            this.text = text;
        }

        @Override
        public Object getData(DataFlavor dataFlavor) throws Exception {
            return text;
        }
    }
}