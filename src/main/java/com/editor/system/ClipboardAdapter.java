package com.editor.system;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *  This class helps to insert text correctly
 */
public class ClipboardAdapter {
    public String getText() {
        Clipboard systemClipboard = getSystemClipboard();
        DataFlavor dataFlavor = DataFlavor.stringFlavor;

        if (systemClipboard.isDataFlavorAvailable(dataFlavor)) {
            Object text = null;
            try {
                text = systemClipboard.getData(dataFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
            return (String) text;
        }

        return null;
    }

    public void setText(String text) {
        getSystemClipboard().setContents(new StringSelection(text), null);
    }

    private static Clipboard getSystemClipboard() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

        return systemClipboard;
    }


}
