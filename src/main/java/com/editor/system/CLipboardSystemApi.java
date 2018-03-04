package com.editor.system;

import java.awt.datatransfer.DataFlavor;

public interface ClipboardSystemApi {
    boolean isDataFlavorAvailable(DataFlavor dataFlavor);

    void setContents(String text);

    Object getData(DataFlavor dataFlavor) throws Exception;
}
