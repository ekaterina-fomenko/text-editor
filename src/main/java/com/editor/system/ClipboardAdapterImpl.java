package com.editor.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.datatransfer.DataFlavor;
import java.util.Optional;

/**
 * This class helps to insert text correctly
 */
public class ClipboardAdapterImpl implements ClipboardAdapter {
    private static Logger log = LoggerFactory.getLogger(ClipboardAdapterImpl.class);
    private ClipboardSystemApi clipboardSystemApi;

    public ClipboardAdapterImpl(ClipboardSystemApi clipboardSystemApi) {
        this.clipboardSystemApi = clipboardSystemApi;
    }

    @Override
    public Optional<String> getText() {
        if (clipboardSystemApi.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            Object text = null;
            try {
                text = clipboardSystemApi.getData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                log.error("Could not read from clipboard", e);
            }
            return text instanceof String ? Optional.of((String) text) : Optional.empty();
        }

        return Optional.empty();
    }

    @Override
    public void setText(String text) {
        clipboardSystemApi.setContents(text);
    }

}
