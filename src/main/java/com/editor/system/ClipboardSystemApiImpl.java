package com.editor.system;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public class ClipboardSystemApiImpl implements ClipboardSystemApi {
    @Override
    public boolean isDataFlavorAvailable(DataFlavor dataFlavor) {
        return getSystemClipboard().isDataFlavorAvailable(dataFlavor);
    }

    @Override
    public void setContents(String text) {
        getSystemClipboard().setContents(new StringSelection(text), null);
    }

    @Override
    public Object getData(DataFlavor dataFlavor) throws Exception {
        return getSystemClipboard().getData(dataFlavor);
    }

    private static Clipboard getSystemClipboard() {
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }
}